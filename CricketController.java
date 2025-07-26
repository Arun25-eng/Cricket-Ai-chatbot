package com.cricket.chatbot.controller;

import com.cricket.chatbot.service.CricketKnowledgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CricketController {
    private final CricketKnowledgeService knowledgeService;

    public CricketController(CricketKnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping("/api/most-centuries")
    public String mostCenturies() {
        return knowledgeService.getMostCenturies();
    }

    // Add endpoints for other facts
}