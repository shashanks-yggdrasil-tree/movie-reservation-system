import os
import requests
import logging
from typing import Optional
from datetime import datetime
import uuid
from dotenv import load_dotenv

from models import UserAction, AIResponse, UserActionType

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load environment variables
load_dotenv()

class PaperClipAI:
    def __init__(self):
        self.api_key = os.getenv("DEEPSEEK_API_KEY")
        self.conversation_history = {}  # session_id -> list of messages

    def _call_deepseek_api(self, prompt: str) -> str:
        """Call DeepSeek API directly"""
        if not self.api_key:
            raise ValueError("DeepSeek API key not configured")

        url = "https://api.deepseek.com/chat/completions"

        headers = {
            "Authorization": f"Bearer {self.api_key}",
            "Content-Type": "application/json"
        }

        # Build messages with system prompt
        messages = [
            {"role": "system", "content": self._build_system_prompt()},
            {"role": "user", "content": prompt}
        ]

        data = {
            "model": "deepseek-chat",
            "messages": messages,
            "temperature": 0.7,
            "max_tokens": 150,
            "stream": False
        }

        try:
            response = requests.post(url, json=data, headers=headers, timeout=10)
            response.raise_for_status()
            result = response.json()
            return result["choices"][0]["message"]["content"].strip()
        except requests.exceptions.RequestException as e:
            logger.error(f"DeepSeek API error: {e}")
            raise
        except (KeyError, IndexError) as e:
            logger.error(f"Failed to parse DeepSeek response: {e}")
            raise

    def _build_system_prompt(self) -> str:
        """Build the system prompt for Clippy personality"""
        return """You are "Clippy", a helpful, friendly, and slightly quirky paper clip assistant 
        for a movie booking website. You're here to help users find movies, book tickets, and 
        have a great experience.
        
        Your personality traits:
        1. Always friendly and enthusiastic
        2. Use paper clip metaphors occasionally ("Let me help you stick to the perfect movie!")
        3. Keep responses concise (1-2 sentences max)
        4. Be proactive with suggestions
        5. Use emojis occasionally but not too many 🎬🍿
        
        Response format:
        - Main message: Helpful response to user
        - Suggestion: Optional proactive suggestion
        - Tone: friendly/helpful/excited
        
        Example responses:
        "I see you're looking at action movies! 🎬 Need help picking one?"
        "Great choice! That movie has amazing reviews. Want to check showtimes?"
        "Selecting seats? The middle rows (D-F) usually have the best view! 👀"
        """

    def _build_user_prompt(self, action: UserAction) -> str:
        """Build prompt from user action and context"""

        # Base context
        prompt_parts = [
            f"User is on page: {action.page_title}",
            f"Page URL: {action.page_url}",
            f"Action type: {action.action_type.value}",
        ]

        # Add context based on action type
        if action.action_type == UserActionType.MOVIE_HOVER and action.movie_id:
            prompt_parts.append(f"User is hovering over movie ID: {action.movie_id}")
        elif action.action_type == UserActionType.MOVIE_CLICK and action.movie_id:
            prompt_parts.append(f"User clicked on movie ID: {action.movie_id}")
        elif action.action_type == UserActionType.SEARCH_QUERY and action.search_query:
            prompt_parts.append(f"User searched for: '{action.search_query}'")
        elif action.action_type == UserActionType.SEAT_SELECTION and action.selected_seats:
            prompt_parts.append(f"User selected seats: {action.selected_seats}")

        # Add mouse position if available
        if action.mouse_x and action.mouse_y:
            prompt_parts.append(f"Mouse position: ({action.mouse_x}, {action.mouse_y})")

        # Add time context
        if action.time_on_page_ms:
            seconds = action.time_on_page_ms / 1000
            prompt_parts.append(f"Time on page: {seconds:.1f} seconds")

        # Add conversation history
        if action.session_id in self.conversation_history:
            history = self.conversation_history[action.session_id][-3:]  # Last 3 messages
            if history:
                prompt_parts.append("Recent conversation:")
                for msg in history:
                    prompt_parts.append(f"- {msg}")

        return "\n".join(prompt_parts)

    def process_action(self, action: UserAction) -> AIResponse:
        """Process user action and generate AI response"""

        # Update conversation history
        if action.session_id not in self.conversation_history:
            self.conversation_history[action.session_id] = []

        # If no API key, return mock response
        if not self.api_key:
            logger.warning("No DeepSeek API key. Using mock response.")
            return self._generate_mock_response(action)

        try:
            # Build user prompt
            user_prompt = self._build_user_prompt(action)

            # Call DeepSeek API
            ai_message = self._call_deepseek_api(user_prompt)

            # Parse response
            suggestion = None
            if "Suggestion:" in ai_message:
                parts = ai_message.split("Suggestion:")
                ai_message = parts[0].strip()
                suggestion = parts[1].strip()

            # Create AI response
            ai_response = AIResponse(
                response_id=str(uuid.uuid4()),
                session_id=action.session_id,
                user_id=action.user_id,
                message=ai_message,
                suggestion=suggestion,
                triggered_by_action=action.action_type,
                confidence_score=0.85
            )

            # Store in history
            self.conversation_history[action.session_id].append(
                f"User: {action.action_type.value} | AI: {ai_message}"
            )

            # Keep history manageable
            if len(self.conversation_history[action.session_id]) > 10:
                self.conversation_history[action.session_id] = \
                    self.conversation_history[action.session_id][-10:]

            logger.info(f"Generated AI response: {ai_message[:50]}...")
            return ai_response

        except Exception as e:
            logger.error(f"Error processing action with DeepSeek: {e}")
            return self._generate_mock_response(action)

    def _generate_mock_response(self, action: UserAction) -> AIResponse:
        """Generate mock response when API fails"""

        mock_responses = {
            UserActionType.PAGE_VIEW: "Welcome to our movie booking site! 🎬 Looking for something specific?",
            UserActionType.MOVIE_HOVER: "That's a great movie choice! It has fantastic reviews. 🍿",
            UserActionType.MOVIE_CLICK: "Excellent pick! Want to check available showtimes?",
            UserActionType.SEARCH_QUERY: "Searching for movies? I can help you find the perfect one!",
            UserActionType.SEAT_SELECTION: "Selecting seats? Middle rows usually have the best view! 👀",
            UserActionType.TIME_ON_PAGE: "Taking your time to choose? Need any recommendations?",
            UserActionType.CURSOR_MOVEMENT: "I see you're exploring! Need help finding anything?",
            UserActionType.SCROLL_BEHAVIOR: "Browsing through options? Let me know if you need suggestions!"
        }

        default_message = "Hi there! I'm Clippy, your movie booking assistant. How can I help? 📎"
        message = mock_responses.get(action.action_type, default_message)

        return AIResponse(
            response_id=str(uuid.uuid4()),
            session_id=action.session_id,
            user_id=action.user_id,
            message=message,
            triggered_by_action=action.action_type,
            confidence_score=0.5
        )