package buzzer_game.service;

import buzzer_game.leaderBoard.Leaderboard;
import buzzer_game.models.Player;
import buzzer_game.models.ScoreEvent;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLeaderboard implements Leaderboard {
    // Maps gameId -> (Player -> total points earned in that game)
    private final Map<String, Map<Player, Integer>> gameScores = new ConcurrentHashMap<>();

    @Override
    public void onScoreUpdate(ScoreEvent event) {
        gameScores.computeIfAbsent(event.getGameId(), k -> new ConcurrentHashMap<>())
                .merge(event.getPlayer(), event.getPointsAwarded(), Integer::sum);
    }

    public void displayGame(String gameId) {
        System.out.println("\n=== GAME LEADERBOARD: " + gameId + " ===");
        Map<Player, Integer> scores = gameScores.get(gameId);
        if (scores == null || scores.isEmpty()) {
            System.out.println("No scores recorded for this game.");
        } else {
            scores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(e -> System.out.println(e.getKey().getName() + ": " + e.getValue() + " pts"));
        }
        System.out.println("=========================================");
    }

    @Override
    public void display() {
        gameScores.keySet().forEach(this::displayGame);
    }
}