package buzzer_game.leaderBoard;

import buzzer_game.leaderBoard.Leaderboard;
import buzzer_game.models.Player;
import buzzer_game.models.ScoreEvent;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoundLeaderboard implements Leaderboard {
    // Maps roundId -> (Player -> total points earned in that round)
    private final Map<String, Map<Player, Integer>> roundScores = new ConcurrentHashMap<>();

    @Override
    public void onScoreUpdate(ScoreEvent event) {
        roundScores.computeIfAbsent(event.getRoundId(), k -> new ConcurrentHashMap<>())
                .merge(event.getPlayer(), event.getPointsAwarded(), Integer::sum);
    }

    public void displayRound(String roundId) {
        System.out.println("\n=== ROUND LEADERBOARD: " + roundId + " ===");
        Map<Player, Integer> scores = roundScores.get(roundId);
        if (scores == null || scores.isEmpty()) {
            System.out.println("No scores recorded for this round.");
        } else {
            scores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(e -> System.out.println(e.getKey().getName() + ": " + e.getValue() + " pts"));
        }
        System.out.println("==========================================");
    }

    @Override
    public void display() {
        // Display all tracked rounds
        roundScores.keySet().forEach(this::displayRound);
    }
}