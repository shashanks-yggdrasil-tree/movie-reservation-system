package com.theater.movie_reservation_system.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

@Service
public class WebSocketService {
	
	private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);
	
	private final SimpMessagingTemplate messagingTemplate;
	private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
	
	public WebSocketService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	public void sendToSession(String sessionId, String message) {
		try {
			// Send to specific session
			messagingTemplate.convertAndSendToUser(
					sessionId,
					"/queue/ai-responses",
					createAIResponseMessage(message)
			);
			
			// Also broadcast to topic for general subscribers
			messagingTemplate.convertAndSend(
					"/topic/ai-responses",
					(Object) createBroadcastMessage(sessionId, message)
			);
			
			log.debug("Sent AI response to session {}: {}", sessionId, message);
			
		} catch (Exception e) {
			log.error("Error sending WebSocket message", e);
		}
	}
	
	public void registerSession(String sessionId, String username) {
		sessionUserMap.put(sessionId, username);
		log.info("Registered WebSocket session {} for user {}", sessionId, username);
	}
	
	public void unregisterSession(String sessionId) {
		sessionUserMap.remove(sessionId);
		log.info("Unregistered WebSocket session {}", sessionId);
	}
	
	private Map<String, Object> createAIResponseMessage(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("type", "AI_RESPONSE");
		response.put("message", message);
		response.put("timestamp", System.currentTimeMillis());
		return response;
	}
	
	private Map<String, Object> createBroadcastMessage(String sessionId, String message) {
		Map<String, Object> broadcast = new HashMap<>();
		broadcast.put("sessionId", sessionId);
		broadcast.put("message", message);
		broadcast.put("timestamp", System.currentTimeMillis());
		broadcast.put("user", sessionUserMap.getOrDefault(sessionId, "anonymous"));
		return broadcast;
	}
}