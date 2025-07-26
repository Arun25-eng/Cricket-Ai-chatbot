package com.cricket.chatbot.controller;

import com.cricket.chatbot.dto.ChatMessageDTO;
import com.cricket.chatbot.dto.ChatResponseDTO;
import com.cricket.chatbot.entity.ChatMessage;
import com.cricket.chatbot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@Validated
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @PostMapping("/message")
    public ResponseEntity<ChatResponseDTO> sendMessage(@Valid @RequestBody ChatMessageDTO messageDTO) {
        try {
            ChatResponseDTO response = chatService.processMessage(messageDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ChatResponseDTO errorResponse = new ChatResponseDTO(
                "Sorry, I'm having trouble processing your message. Please try again.", 
                messageDTO.getSessionId()
            );
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String sessionId) {
        List<ChatMessage> history = chatService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/session/new")
    public ResponseEntity<Map<String, String>> createNewSession() {
        String sessionId = chatService.generateSessionId();
        return ResponseEntity.ok(Map.of("sessionId", sessionId));
    }
    
    @GetMapping("/session/{sessionId}/count")
    public ResponseEntity<Map<String, Long>> getMessageCount(@PathVariable String sessionId) {
        long count = chatService.getMessageCount(sessionId);
        return ResponseEntity.ok(Map.of("messageCount", count));
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Cricket AI Chatbot",
            "version", "1.0.0"
        ));
    }
}