from pydantic import BaseModel
from typing import Optional, Dict, Any, List
from datetime import datetime
from enum import Enum

class UserActionType(str, Enum):
    PAGE_VIEW = "PAGE_VIEW"
    MOVIE_HOVER = "MOVIE_HOVER"
    MOVIE_CLICK = "MOVIE_CLICK"
    SEAT_SELECTION = "SEAT_SELECTION"
    SEARCH_QUERY = "SEARCH_QUERY"
    TIME_ON_PAGE = "TIME_ON_PAGE"
    CURSOR_MOVEMENT = "CURSOR_MOVEMENT"
    SCROLL_BEHAVIOR = "SCROLL_BEHAVIOR"

class UserAction(BaseModel):
    """Model for user behavior events"""
    session_id: str
    user_id: Optional[str] = None
    action_type: UserActionType
    page_url: Optional[str] = None
    page_title: str

    # Mouse/Interaction data
    mouse_x: Optional[int] = None
    mouse_y: Optional[int] = None
    click_target: Optional[str] = None
    hover_duration_ms: Optional[int] = None

    # Content context
    movie_id: Optional[int] = None
    movie_name: Optional[str] = None
    theater_id: Optional[int] = None
    search_query: Optional[str] = None
    selected_seats: Optional[List[int]] = None

    # Timing
    timestamp: datetime = datetime.utcnow()
    time_on_page_ms: Optional[int] = None

    # Additional metadata
    metadata: Dict[str, Any] = {}

class AIResponse(BaseModel):
    """Model for AI-generated responses"""
    response_id: str
    session_id: str
    user_id: Optional[str] = None

    # Response content
    message: str
    suggestion: Optional[str] = None
    action_recommendation: Optional[str] = None
    emotion_tone: str = "friendly"  # friendly, excited, helpful, etc.

    # Context
    triggered_by_action: UserActionType
    confidence_score: float = 0.8  # How confident AI is

    # Timing
    generated_at: datetime = datetime.utcnow()
    ttl_seconds: int = 300  # Time to live (5 minutes)