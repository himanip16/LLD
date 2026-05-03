package buzzer_game.leaderBoard;

import buzzer_game.leaderBoard.Leaderboard;
import buzzer_game.models.Player;
import buzzer_game.models.ScoreEvent;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalLeaderboard implements Leaderboard {
    // Maps Player -> Cumulative lifetime points
    private final Map<Player, Integer> globalScores = new ConcurrentHashMap<>();

    @Override
    public void onScoreUpdate(ScoreEvent event) {
        globalScores.merge(event.getPlayer(), event.getPointsAwarded(), Integer::sum);
    }

    @Override
    public void display() {
        System.out.println("\n=== GLOBAL ALL-TIME LEADERBOARD ===");
        if (globalScores.isEmpty()) {
            System.out.println("No global scores recorded.");
        } else {
            globalScores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(e -> System.out.println(e.getKey().getName() + ": " + e.getValue() + " pts"));
        }
        System.out.println("====================================\n");
    }
}