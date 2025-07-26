package com.cricket.chatbot.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CricketAiService {
    
    private final Map<String, List<String>> cricketKnowledge;
    private final List<String> greetings;
    private final Random random;
    
    public CricketAiService() {
        this.random = new Random();
        this.greetings = Arrays.asList(
            "Hello! I'm your Cricket AI assistant. How can I help you today?",
            "Hi there! Ready to talk about cricket?",
            "Welcome! Ask me anything about cricket!"
        );
        
        this.cricketKnowledge = initializeCricketKnowledge();
    }
    
    public String generateResponse(String userMessage) {
        String message = userMessage.toLowerCase().trim();
        
        if (isGreeting(message)) {
            return greetings.get(random.nextInt(greetings.size()));
        }

        // Specific cricket facts and player queries
        if (message.contains("sachin")) {
            return "Sachin Tendulkar is a legendary Indian cricketer, the highest run-scorer in international cricket and the first to score 100 international centuries.";
        }
        if (message.contains("virat kohli")) {
            return "Virat Kohli is a modern great, known for his consistency and aggressive batting across all formats. He has the most runs in IPL history.";
        }
        if (message.contains("ms dhoni")) {
            return "MS Dhoni is a former Indian captain, famous for his calm leadership, finishing skills, and leading India to multiple ICC trophies.";
        }
        if (message.contains("don bradman")) {
            return "Sir Don Bradman is considered the greatest batsman in cricket history, with a Test average of 99.94.";
        }
        if (message.contains("ab de villiers")) {
            return "AB de Villiers, known as Mr. 360, is famous for his innovative and explosive batting in all formats.";
        }
        if (message.contains("most runs in ipl")) {
            return "Virat Kohli has scored the most runs in IPL history.";
        }
        if (message.contains("most runs in international")) {
            return "Sachin Tendulkar holds the record for the most runs in international cricket (34,357 runs).";
        }
        if (message.contains("most sixes")) {
            return "Chris Gayle holds the record for the most sixes in international cricket.";
        }
        if (message.contains("most fours")) {
            return "Sachin Tendulkar has hit the most fours in international cricket.";
        }
        if (message.contains("most hundreds")) {
            return "Sachin Tendulkar has scored the most international hundreds (100 centuries).";
        }
        if (message.contains("most wickets")) {
            return "Muttiah Muralitharan holds the record for the most wickets in international cricket (1,347 wickets).";
        }
        if (message.contains("top bowlers")) {
            return "Some of the greatest bowlers include Muttiah Muralitharan, Shane Warne, Wasim Akram, Glenn McGrath, and Anil Kumble.";
        }
        if (message.contains("pitch types") || message.contains("types of pitch")) {
            return "Cricket pitches can be hard, green, dusty, or dead. Hard pitches favor fast bowlers, green pitches assist seamers, dusty pitches help spinners, and dead pitches are flat and good for batting.";
        }
        if (message.contains("teach me how to bat") || message.contains("how to bat")) {
            return "To bat well: use a balanced stance, keep your eyes on the ball, practice footwork, play with soft hands, and focus on timing. Watch great batsmen and practice regularly.";
        }
        if (message.contains("improve batting technique")) {
            return "Work on your grip, stance, and backlift. Practice playing straight, timing the ball, and rotating the strike. Drills and coaching can help refine your technique.";
        }
        if (message.contains("improve bowling technique") || message.contains("how to bowl")) {
            return "To improve bowling: focus on a smooth run-up, consistent line and length, wrist position, and follow-through. Learn to bowl different deliveries like yorkers, bouncers, and slower balls.";
        }
        if (message.contains("rules") || message.contains("laws")) {
            return "Cricket has 42 laws. Key rules: 11 players per team, 6 balls per over, ways to get out (bowled, caught, LBW, run out, stumped), powerplay restrictions, and fielding limitations. Ask about any specific rule!";
        }
        if (message.contains("lbw")) {
            return "LBW (Leg Before Wicket) is a way of getting out. If the ball hits the batsman's leg in line with the stumps and would have hit the stumps, the batsman is out LBW (with some exceptions).";
        }
        if (message.contains("powerplay")) {
            return "Powerplay is a set of overs in limited-overs cricket with fielding restrictions, allowing only a few fielders outside the 30-yard circle.";
        }
        if (message.contains("fielding positions")) {
            return "Common fielding positions: slip, gully, point, cover, mid-off, mid-on, square leg, fine leg, third man, long on, long off, and more.";
        }
        if (message.contains("captain")) {
            return "The captain leads the team, sets field placements, and makes tactical decisions. Famous captains include MS Dhoni, Ricky Ponting, and Clive Lloyd.";
        }
        if (message.contains("format") || message.contains("type")) {
            return "Cricket has three main formats: Test Cricket (5 days), One Day Internationals (50 overs), and T20 (20 overs). Each format has its unique characteristics and strategies.";
        }
        if (message.contains("player")) {
            return "Cricket has produced legendary players like Sachin Tendulkar, Sir Don Bradman, Virat Kohli, AB de Villiers, MS Dhoni, and many more. Each brought unique skills to the game!";
        }
        
        // Match cricket topics
        for (Map.Entry<String, List<String>> entry : cricketKnowledge.entrySet()) {
            if (message.contains(entry.getKey())) {
                List<String> responses = entry.getValue();
                return responses.get(random.nextInt(responses.size()));
            }
        }
        
        return "That's an interesting cricket question! While I may not have specific details about that, I'd love to help you with information about cricket rules, players, formats, techniques, or history. What would you like to know?";
    }
    
    private boolean isGreeting(String message) {
        String[] greetingWords = {"hello", "hi", "hey", "good morning", "good afternoon", "good evening"};
        return Arrays.stream(greetingWords).anyMatch(message::contains);
    }
    
    private Map<String, List<String>> initializeCricketKnowledge() {
        Map<String, List<String>> knowledge = new HashMap<>();
        
        knowledge.put("batting", Arrays.asList(
            "Batting is the art of scoring runs while protecting your wicket. Key techniques include proper stance, head position, and timing.",
            "Great batsmen focus on playing straight, watching the ball closely, and building partnerships.",
            "Batting requires patience, concentration, and the ability to rotate strike effectively.",
            "To score a hundred, a batsman must build an innings, rotate strike, and punish bad balls."
        ));
        
        knowledge.put("bowling", Arrays.asList(
            "Bowling is about taking wickets and restricting runs. There are fast bowlers, spinners, and medium pacers.",
            "Good bowling requires consistent line and length, variation in pace, and reading the batsman.",
            "Bowlers use different deliveries like yorkers, bouncers, and slower balls to deceive batsmen.",
            "Top wicket-takers include Muttiah Muralitharan, Shane Warne, and Anil Kumble."
        ));
        
        knowledge.put("fielding", Arrays.asList(
            "Fielding is crucial in modern cricket. It involves catching, stopping boundaries, and running out batsmen.",
            "Good fielding can save 20-30 runs per innings and create pressure on the batting team.",
            "Key fielding skills include anticipation, quick reflexes, and accurate throwing.",
            "Fielding positions include slip, gully, point, cover, mid-off, mid-on, square leg, and fine leg."
        ));
        
        knowledge.put("ipl", Arrays.asList(
            "The Indian Premier League (IPL) is the world's most popular T20 league, featuring top international players.",
            "IPL has 10 teams and follows a franchise-based model with player auctions.",
            "The tournament has revolutionized cricket with its entertainment value and financial impact.",
            "Virat Kohli has the most runs in IPL history. Chris Gayle has hit the most sixes."
        ));
        
        knowledge.put("world cup", Arrays.asList(
            "The Cricket World Cup is held every four years and is cricket's premier tournament.",
            "Australia has won the most World Cups (5 times), followed by West Indies and India.",
            "The World Cup features the best teams competing in a format that tests all aspects of cricket.",
            "Sachin Tendulkar has the most runs in World Cup history."
        ));
        
        knowledge.put("test cricket", Arrays.asList(
            "Test cricket is the longest format, played over 5 days with two innings per team.",
            "It's considered the ultimate test of a cricketer's skill, endurance, and mental strength.",
            "Test cricket requires different strategies compared to limited-overs formats.",
            "Sir Don Bradman is the greatest Test batsman with an average of 99.94."
        ));

        knowledge.put("pitch", Arrays.asList(
            "Cricket pitches can be hard, green, dusty, or dead. Hard pitches favor fast bowlers, green pitches assist seamers, dusty pitches help spinners, and dead pitches are flat and good for batting.",
            "Pitch conditions play a crucial role in determining match strategy and team selection."
        ));

        knowledge.put("hundred", Arrays.asList(
            "A century (hundred) is when a batsman scores 100 or more runs in a single innings.",
            "Sachin Tendulkar has the most international hundreds (100)."
        ));

        knowledge.put("six", Arrays.asList(
            "A six is scored when the batsman hits the ball over the boundary without it touching the ground.",
            "Chris Gayle has hit the most sixes in international and IPL cricket."
        ));

        knowledge.put("four", Arrays.asList(
            "A four is scored when the batsman hits the ball to the boundary along the ground.",
            "Sachin Tendulkar has hit the most fours in international cricket."
        ));

        knowledge.put("wicket", Arrays.asList(
            "A wicket can refer to the stumps, or a batsman being dismissed.",
            "Muttiah Muralitharan has the most wickets in international cricket."
        ));

        knowledge.put("technique", Arrays.asList(
            "Good technique is essential for both batting and bowling. It involves proper grip, stance, footwork, and shot selection for batsmen; and run-up, action, and follow-through for bowlers.",
            "Coaching and regular practice are key to improving cricket technique."
        ));
        
        return knowledge;
    }
}