import sys
import os

# Force add /app/src to Python path
sys.path.insert(0, '/app/src')

from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from fastapi.middleware.cors import CORSMiddleware
import logging
import json
import asyncio
from threading import Thread

from models import UserAction, AIResponse
from kafka_client import KafkaClient
from ai_processor import PaperClipAI

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize
app = FastAPI(title="PaperClip AI Assistant", version="1.0.0")

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, restrict to your frontend domain
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global instances
kafka_client = KafkaClient()
ai_processor = PaperClipAI()
connected_websockets = []

class ConnectionManager:
    def __init__(self):
        self.active_connections: list[WebSocket] = []

    async def connect(self, websocket: WebSocket):
        await websocket.accept()
        self.active_connections.append(websocket)
        logger.info(f"New WebSocket connection. Total: {len(self.active_connections)}")

    def disconnect(self, websocket: WebSocket):
        self.active_connections.remove(websocket)
        logger.info(f"WebSocket disconnected. Total: {len(self.active_connections)}")

    async def broadcast(self, message: dict):
        """Broadcast message to all connected clients"""
        disconnected = []
        for connection in self.active_connections:
            try:
                await connection.send_json(message)
            except Exception as e:
                logger.error(f"Failed to send to WebSocket: {e}")
                disconnected.append(connection)

        # Clean up disconnected clients
        for connection in disconnected:
            self.disconnect(connection)

manager = ConnectionManager()

def kafka_consumer_thread():
    """Thread function to consume from Kafka"""

    def process_user_action(message: dict):
        """Process incoming user action from Kafka"""
        try:
            # Convert to UserAction model
            user_action = UserAction(**message)
            logger.info(f"Processing action: {user_action.action_type}")

            # Generate AI response
            ai_response = ai_processor.process_action(user_action)

            # Send to Kafka for Spring Boot to pick up
            kafka_client.produce(
                topic="ai-responses",
                key=user_action.session_id,
                value=ai_response.dict()
            )

            # Also broadcast via WebSocket if anyone is listening
            asyncio.run(manager.broadcast(ai_response.dict()))

        except Exception as e:
            logger.error(f"Error processing Kafka message: {e}")

    # Start consuming from user-actions topic
    kafka_client.consume(
        topics=["user-actions"],
        callback=process_user_action
    )

@app.on_event("startup")
async def startup_event():
    """Initialize on startup"""
    logger.info("Starting PaperClip AI Assistant...")

    # Start Kafka client
    # kafka_client.start()

    # Start Kafka consumer in background thread
    # consumer_thread = Thread(target=kafka_consumer_thread, daemon=True)
    # consumer_thread.start()

    logger.info("AI Assistant started successfully!")

@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down AI Assistant...")
    kafka_client.stop()
    logger.info("AI Assistant shutdown complete")

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "service": "paperclip-ai",
        "websocket_connections": len(manager.active_connections)
    }

@app.post("/api/process-action")
async def process_action_directly(action: UserAction):
    """Direct API endpoint for testing (bypasses Kafka)"""
    logger.info(f"Direct action processing: {action.action_type}")

    ai_response = ai_processor.process_action(action)

    # Also send to Kafka for consistency
    kafka_client.produce(
        topic="ai-responses",
        key=action.session_id,
        value=ai_response.dict()
    )

    return ai_response

@app.websocket("/ws/ai")
async def websocket_endpoint(websocket: WebSocket):
    """WebSocket endpoint for real-time AI responses"""
    await manager.connect(websocket)

    try:
        while True:
            # Receive user action directly via WebSocket
            data = await websocket.receive_json()

            # Process immediately
            user_action = UserAction(**data)
            ai_response = ai_processor.process_action(user_action)

            # Send response back to same client
            await websocket.send_json(ai_response.dict())

    except WebSocketDisconnect:
        manager.disconnect(websocket)
    except Exception as e:
        logger.error(f"WebSocket error: {e}")
        manager.disconnect(websocket)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    )