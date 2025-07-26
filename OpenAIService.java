package com.cricket.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class OpenAIService {
    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;
    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String askOpenAI(String question) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", question)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, entity, Map.class);
            Map choices = ((List<Map>) response.getBody().get("choices")).get(0);
            Map message = (Map) choices.get("message");
            return message.get("content").toString();
        } catch (Exception e) {
            return "Sorry, I couldn't get an answer from OpenAI.";
        }
    }
}