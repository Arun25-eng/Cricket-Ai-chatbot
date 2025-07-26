package com.cricket.chatbot.dto;

import java.time.LocalDateTime;

public class ChatResponseDTO {
    
    private String response;
    private LocalDateTime timestamp;
    private String sessionId;
    
    public ChatResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ChatResponseDTO(String response, String sessionId) {
        this.response = response;
        this.sessionId = sessionId;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}