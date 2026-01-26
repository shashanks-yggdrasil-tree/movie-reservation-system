// KafkaConsumerService.java
package com.theater.movie_reservation_system.service.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.theater.movie_reservation_system.service.WebSocketService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
public class KafkaConsumerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
	
	private final ObjectMapper objectMapper;
	private final WebSocketService webSocketService;
	
	public KafkaConsumerService(ObjectMapper objectMapper,
	                            WebSocketService webSocketService
	) {
		this.objectMapper = objectMapper;
		this.webSocketService = webSocketService;
	}
	
	@KafkaListener(topics = "${kafka.topics.ai-responses}", groupId = "spring-boot-consumer")
	public void consumeAIResponse(String message) {
		try {
			log.info("Received AI response from Kafka: {}", message);
			
			// Parse the message
			Map<String, Object> response = objectMapper.readValue(message, Map.class);
			
			String sessionId = (String) response.get("session_id");
			String aiMessage = (String) response.get("message");
			String source = (String) response.get("source");
			
			log.info("AI Response for session {}: {}", sessionId, aiMessage);
			
			// Send to WebSocket
			webSocketService.sendToSession(sessionId, aiMessage);
			
			// You could also store in database or cache
			// aiResponseService.saveResponse(sessionId, aiMessage);
			
		} catch (Exception e) {
			log.error("Error processing AI response from Kafka", e);
		}
	}
}