package buzzer_game.score;

import buzzer_game.models.Player;
import buzzer_game.models.QuestionSession;
import buzzer_game.models.Response;

public class SpeedBasedScoreStrategy implements ScoreStrategy {
    private final int basePoints;

    // Configuration constants
    private static final long FAST_RESPONSE_THRESHOLD_MS = 2000;
    private static final long MEDIUM_RESPONSE_THRESHOLD_MS = 5000;
    private static final int FAST_BONUS = 15;
    private static final int MEDIUM_BONUS = 5;

    public SpeedBasedScoreStrategy(int basePoints) {
        this.basePoints = basePoints;
    }

    @Override
    public int calculatePoints(QuestionSession session, Player player) {
        // Find the response submitted by this player in the current question session
        Response playerResponse = session.getResponses().stream()
                .filter(r -> r.getPlayer().equals(player))
                .findFirst()
                .orElse(null);

        // If no response was recorded or option was wrong, they get 0 points
        if (playerResponse == null || !playerResponse.getChosenOption().isCorrect()) {
            return 0;
        }

        long responseTimeMs = playerResponse.getTimeTakenMs();

        if (responseTimeMs < FAST_RESPONSE_THRESHOLD_MS) {
            return basePoints + FAST_BONUS;
        } else if (responseTimeMs < MEDIUM_RESPONSE_THRESHOLD_MS) {
            return basePoints + MEDIUM_BONUS;
        }

        return basePoints;
    }
}