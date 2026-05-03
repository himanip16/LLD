package buzzer_game.models;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks scores for all players within a single game instance.
 * Mutation is only possible through its own methods — no raw map exposure.
 */
public class ScoreCard {

    @Getter
    private final String gameId; // Fixed: Changed from int to String
    private final Map<Player, Integer> playerScores = new ConcurrentHashMap<>();

    public ScoreCard(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Registers a player with 0 points. No-op if already registered.
     */
    public void initPlayer(Player player) {
        playerScores.putIfAbsent(player, 0);
    }

    /**
     * Atomically adds points to a player's score.
     */
    public void addScore(Player player, int points) {
        playerScores.merge(player, points, Integer::sum);
    }

    /**
     * Returns an unmodifiable view of current scores.
     * Callers cannot mutate the underlying map.
     */
    public Map<Player, Integer> getPlayerScores() {
        return Collections.unmodifiableMap(playerScores);
    }

    public int getScore(Player player) {
        return playerScores.getOrDefault(player, 0);
    }
}