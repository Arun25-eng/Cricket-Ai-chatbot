package com.cricket.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
public class CricketKnowledgeService {
    private JsonNode facts;

    public CricketKnowledgeService() {
        try (InputStream is = getClass().getResourceAsStream("/cricket-facts.json")) {
            facts = new ObjectMapper().readTree(is);
        } catch (Exception e) {
            facts = null;
        }
    }

    public String getMostCenturies() {
        if (facts != null && facts.has("most_centuries")) {
            JsonNode mc = facts.get("most_centuries");
            return mc.get("player").asText() + " has the most centuries (" + mc.get("centuries").asInt() + ").";
        }
        return "Data not available.";
    }

    // Add similar methods for wickets, pitch types, etc.
}