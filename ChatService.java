package com.cricket.chatbot.service;

import com.cricket.chatbot.dto.ChatMessageDTO;
import com.cricket.chatbot.dto.ChatResponseDTO;
import com.cricket.chatbot.entity.ChatMessage;
import com.cricket.chatbot.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private CricketAiService cricketAiService;
    
    public ChatResponseDTO processMessage(ChatMessageDTO messageDTO) {
        // Generate session ID if not provided
        String sessionId = messageDTO.getSessionId();
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }
        
        // Generate AI response
        String aiResponse = cricketAiService.generateResponse(messageDTO.getMessage());
        
        // Save to database
        ChatMessage chatMessage = new ChatMessage(
            messageDTO.getMessage(), 
            aiResponse, 
            sessionId
        );
        chatMessageRepository.save(chatMessage);
        
        return new ChatResponseDTO(aiResponse, sessionId);
    }
    
    public List<ChatMessage> getChatHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }
    
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }
    
    public long getMessageCount(String sessionId) {
        return chatMessageRepository.countBySessionId(sessionId);
    }
}