package com.theater.movie_reservation_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class BrowserOpener {
	
	@Autowired
	private Environment environment;  // Fixes 'environment'
	
	
	@EventListener(ApplicationReadyEvent.class)
	public void openBrowserAfterStart() {
		String port = environment.getProperty("local.server.port", "8080");
		String portKafkaUI = environment.getProperty("local.kafka-ui.port", "8081");
		List<String> urls = Arrays.asList(
				"http://localhost:" + port + "/swagger-ui/index.html",
				"http://localhost:" + portKafkaUI + "/ui/clusters/local/consumer-groups",
				"https://roadmap.sh/projects/movie-reservation-system",
				"https://dbdiagram.io/d/695bcd6d39fa3db27b2557f8"
//				"http://localhost:" + port + "/actuator"
		);
		
		openUrls(urls);
	}
	
	private void openUrls(List<String> urls) {
		String os = System.getProperty("os.name").toLowerCase();
		
		for (String url : urls) {
			try {
				Thread.sleep(500); // Small delay between openings
				
				if (os.contains("win")) {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				} else if (os.contains("mac")) {
					Runtime.getRuntime().exec("open " + url);
				} else {
					Runtime.getRuntime().exec("xdg-open " + url);
				}
			} catch (Exception e) {
				log.error("Could not open browser for: {}", url);
			}
		}
	}
}