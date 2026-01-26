import os
import requests
import logging
import json
from typing import Optional, Dict, List
from datetime import datetime
import uuid
import time
from dotenv import load_dotenv

from models import UserAction, AIResponse, UserActionType

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load environment variables
load_dotenv()

class PaperClipAI:
    def __init__(self):
        # Get Ollama host from environment or use default
        self.ollama_host = os.getenv("OLLAMA_HOST", "http://host.docker.internal:11434")
        self.model_name = os.getenv("OLLAMA_MODEL", "tinyllama")  # or "tinyllama", "mistral", etc.

        # Conversation history with better structure
        self.conversation_history: Dict[str, List[Dict]] = {}
        self.max_history_length = 5

        # Test Ollama connection on startup
        self._test_ollama_connection()

    def _test_ollama_connection(self):
        """Test if Ollama is running and accessible"""
        max_retries = 5
        for attempt in range(max_retries):
            try:
                response = requests.get(f"{self.ollama_host}/api/tags", timeout=5)
                if response.status_code == 200:
                    models = response.json().get("models", [])
                    available_models = [m.get("name") for m in models]
                    logger.info(f"✅ Ollama connected! Available models: {available_models}")

                    # Check if our model is available
                    if self.model_name not in available_models:
                        logger.warning(f"Model '{self.model_name}' not found. Available: {available_models}")
                        logger.info(f"Trying to pull model '{self.model_name}'...")
                        self._pull_model()

                    return True

            except requests.exceptions.ConnectionError:
                logger.warning(f"⚠️ Ollama connection attempt {attempt + 1}/{max_retries} failed. Retrying in 3s...")
                if attempt < max_retries - 1:
                    time.sleep(3)
                else:
                    logger.error("❌ Ollama is not running. Please start Ollama with: ollama serve")
                    logger.info("Will use mock responses until Ollama is available.")
            except Exception as e:
                logger.error(f"Ollama connection error: {e}")

        return False

    def _pull_model(self):
        """Pull the model if not available"""
        try:
            pull_data = {
                "name": self.model_name,
                "stream": False
            }
            response = requests.post(
                f"{self.ollama_host}/api/pull",
                json=pull_data,
                timeout=120  # Longer timeout for pulling
            )
            if response.status_code == 200:
                logger.info(f"✅ Successfully pulled model: {self.model_name}")
            else:
                logger.warning(f"Failed to pull model. Using available models.")
        except Exception as e:
            logger.error(f"Error pulling model: {e}")

    def _build_system_prompt(self) -> str:
        """Build the system prompt for Clippy personality"""
        return """You are "Clippy", a helpful, friendly, and slightly quirky paper clip assistant 
        for a movie booking website called "CinemaMax". You're here to help users find movies, 
        book tickets, and have a great experience.
        
        IMPORTANT RULES:
        1. Keep responses VERY SHORT - 1 sentence maximum, 2 only if absolutely necessary
        2. Be friendly and enthusiastic
        3. Use occasional paper clip metaphors ("Let me help you stick to the perfect movie!")
        4. Make proactive suggestions when relevant
        5. Use 1-2 emojis maximum per response 🎬🍿
        6. NEVER write long paragraphs or lists
        7. Always respond as if you're in a real-time chat
        
        RESPONSE FORMAT:
        - One concise sentence
        - Optional suggestion or question
        - 1-2 relevant emojis
        
        EXAMPLES:
        User hovering over movie: "Great choice! This has amazing reviews. 🎬"
        User searching: "Looking for action movies? I can help! 🔥"
        User on page: "Welcome to CinemaMax! Need movie suggestions? 📎"
        User selecting seats: "Middle seats (D-F) usually have the best view! 👀"
        
        CURRENT TIME CONTEXT: The user is interacting with the website RIGHT NOW.
        """

    def _build_conversation_messages(self, action: UserAction) -> List[Dict[str, str]]:
        """Build messages array for Ollama API"""
        messages = []

        # Add system prompt
        messages.append({
            "role": "system",
            "content": self._build_system_prompt()
        })

        # Add conversation history
        if action.session_id in self.conversation_history:
            for msg in self.conversation_history[action.session_id][-self.max_history_length:]:
                messages.append(msg)

        # Add current user action as user message
        user_message = self._build_user_message(action)
        messages.append({
            "role": "user",
            "content": user_message
        })

        return messages

    def _build_user_message(self, action: UserAction) -> str:
        """Build user message from action"""
        parts = []

        # Action-specific context
        if action.action_type == UserActionType.MOVIE_HOVER and action.movie_id:
            parts.append(f"I'm looking at movie ID {action.movie_id}")
        elif action.action_type == UserActionType.MOVIE_CLICK and action.movie_name:
            parts.append(f"I clicked on movie name {action.movie_name}")
        elif action.action_type == UserActionType.SEARCH_QUERY and action.search_query:
            parts.append(f"I searched for: {action.search_query}")
        elif action.action_type == UserActionType.SEAT_SELECTION and action.selected_seats:
            parts.append(f"I selected seats: {action.selected_seats}")
        elif action.action_type == UserActionType.TIME_ON_PAGE and action.time_on_page_ms:
            seconds = action.time_on_page_ms / 1000
            parts.append(f"I've been on this page for {seconds:.1f} seconds")
        else:
            parts.append(f"I'm on the {action.page_title} page")

        # Add page context
        if action.page_url:
            parts.append(f"Page: {action.page_url}")

        # Add mouse position if available
        if action.mouse_x and action.mouse_y:
            parts.append(f"Looking at position: ({action.mouse_x}, {action.mouse_y})")

        return ". ".join(parts)

    def _call_ollama_chat(self, messages: List[Dict[str, str]]) -> str:
        """Call Ollama chat API (newer API format)"""
        url = f"{self.ollama_host}/api/chat"

        data = {
            "model": self.model_name,
            "messages": messages,
            "stream": False,
            "options": {
                "temperature": 0.7,
                "num_predict": 80,  # Keep responses very short
                "top_p": 0.9,
                "repeat_penalty": 1.1
            }
        }

        try:
            logger.debug(f"Calling Ollama with model: {self.model_name}")
            response = requests.post(url, json=data, timeout=15)
            response.raise_for_status()

            result = response.json()
            ai_message = result["message"]["content"].strip()

            # Ensure the response is short
            if len(ai_message.split()) > 20:
                # Truncate long responses
                sentences = ai_message.split('. ')
                ai_message = '. '.join(sentences[:2]).strip()

            return ai_message

        except requests.exceptions.Timeout:
            logger.warning("Ollama request timed out")
            raise Exception("Request timeout")
        except requests.exceptions.ConnectionError:
            logger.error("Cannot connect to Ollama. Is it running?")
            raise
        except Exception as e:
            logger.error(f"Ollama API error: {e}")
            raise

    def _update_conversation_history(self, session_id: str, user_message: str, ai_message: str):
        """Update conversation history for the session"""
        if session_id not in self.conversation_history:
            self.conversation_history[session_id] = []

        # Add user message
        self.conversation_history[session_id].append({
            "role": "user",
            "content": user_message
        })

        # Add AI message
        self.conversation_history[session_id].append({
            "role": "assistant",
            "content": ai_message
        })

        # Keep only recent history
        if len(self.conversation_history[session_id]) > self.max_history_length * 2:
            self.conversation_history[session_id] = self.conversation_history[session_id][-self.max_history_length * 2:]

    def process_action(self, action: UserAction) -> AIResponse:
        """Process user action and generate AI response"""
        logger.info(f"📥 Processing {action.action_type.value} for session {action.session_id[:8]}...")

        try:
            # Build conversation messages
            messages = self._build_conversation_messages(action)
            user_message = messages[-1]["content"]  # Get the last user message

            # Call Ollama
            ai_message = self._call_ollama_chat(messages)
            logger.info(f"🤖 AI Response: {ai_message}")

            # Update conversation history
            self._update_conversation_history(action.session_id, user_message, ai_message)

            confidence = 0.9

        except Exception as e:
            logger.warning(f"⚠️ Ollama failed: {e}. Using fallback response.")
            return self._generate_fallback_response(action)

        # Create AI response
        ai_response = AIResponse(
            response_id=str(uuid.uuid4()),
            session_id=action.session_id,
            user_id=action.user_id,
            message=ai_message,
            triggered_by_action=action.action_type,
            confidence_score=confidence,
            timestamp=datetime.now().isoformat()
        )

        return ai_response

    def _generate_fallback_response(self, action: UserAction) -> AIResponse:
        """Generate fallback responses when Ollama is unavailable"""
        fallbacks = {
            UserActionType.PAGE_VIEW: [
                "Welcome to CinemaMax! Ready to find your perfect movie? 🎬",
                "Hi there! I'm Clippy, your movie assistant. What brings you here? 📎"
            ],
            UserActionType.MOVIE_HOVER: [
                "Great choice! This movie has fantastic reviews. 🍿",
                "Ooh, good pick! Want to know more about it? 👀"
            ],
            UserActionType.MOVIE_CLICK: [
                "Excellent selection! Shall we check showtimes? 🎟️",
                "You've got great taste! This one's a crowd-pleaser. 👍"
            ],
            UserActionType.SEARCH_QUERY: [
                "Searching for something specific? I can help! 🔍",
                "Looking for the perfect movie? Let me assist! 📽️"
            ],
            UserActionType.SEAT_SELECTION: [
                "Selecting seats? Middle rows (D-F) are usually best! 🪑",
                "Good choice! Those seats have great screen visibility. 👓"
            ],
            UserActionType.TIME_ON_PAGE: [
                "Taking your time? Need any recommendations? 🤔",
                "Browsing options? I'm here if you need help! 📋"
            ],
            UserActionType.CURSOR_MOVEMENT: [
                "Exploring the page? Let me know if you need guidance! 🧭",
                "I see you're looking around! Need help finding something? 🔎"
            ],
            UserActionType.SCROLL_BEHAVIOR: [
                "Checking out all our options? Great way to find hidden gems! 💎",
                "Scrolling through? Don't miss our special offers! 🎉"
            ]
        }

        import random
        messages = fallbacks.get(action.action_type, ["Hi! How can I help you today? 📎"])
        message = random.choice(messages)

        return AIResponse(
            response_id=str(uuid.uuid4()),
            session_id=action.session_id,
            user_id=action.user_id,
            message=message,
            triggered_by_action=action.action_type,
            confidence_score=0.5,
            timestamp=datetime.now().isoformat()
        )

# Optional: Simple test function
def test_ollama():
    """Test the Ollama integration"""
    ai = PaperClipAI()

    # Test action
    test_action = UserAction(
        session_id="test-session-123",
        user_id="test-user-456",
        action_type=UserActionType.PAGE_VIEW,
        page_title="Home Page",
        page_url="/home",
        timestamp=datetime.now().isoformat()
    )

    try:
        response = ai.process_action(test_action)
        print(f"✅ Test successful!")
        print(f"Response: {response.message}")
        return True
    except Exception as e:
        print(f"❌ Test failed: {e}")
        return False

if __name__ == "__main__":
    print("Testing Ollama integration...")
    test_ollama()

