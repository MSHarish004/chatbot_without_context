
package com.example.ChatBot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Service
public class GrokService {

    private static final String API_KEY =  "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";
    private static final String OPENROUTER_API_URL ="https://openrouter.ai/api/v1";

    private final WebClient webClient;

    public GrokService() {
        this.webClient = WebClient.builder()
                .baseUrl(OPENROUTER_API_URL)
                .defaultHeader("Authorization", "Bearer " + API_KEY)
                .build();
    }

    public String getChatResponse(String userMessage) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek/deepseek-r1-0528:free");
        // The OpenRouter API usually expects messages array like OpenAI chat format
        requestBody.put("messages", java.util.List.of(
                Map.of("role", "user", "content", userMessage), Map.of("role", "system", "content", "You have to respond in english")
        ));
        requestBody.put("temperature", 0);


        Mono<Map> response = webClient.post()
                .uri("/chat/completions")  // Correct endpoint path
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> responseBody = response.block();

        if (responseBody != null && responseBody.containsKey("choices")) {
            var choices = (java.util.List<Map<String, Object>>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null && message.containsKey("content")) {
                    return (String) message.get("content");
                }
            }
        }
        return "No response from OpenRouter AI API.";
    }
}

