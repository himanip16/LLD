package buzzer_game.models;

import lombok.Getter;

@Getter
public class ScoreEvent {
    private final String gameId;
    private final String roundId;
    private final Player player;
    private final int pointsAwarded;

    public ScoreEvent(String gameId, String roundId, Player player, int pointsAwarded) {
        this.gameId = gameId;
        this.roundId = roundId;
        this.player = player;
        this.pointsAwarded = pointsAwarded;
    }
}