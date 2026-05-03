package buzzer_game.score;

import buzzer_game.models.Player;
import buzzer_game.models.QuestionSession;

public interface ScoreStrategy {
    /**
     * Calculates the points awarded to the player.
     * @param session The question session being evaluated.
     * @param player The player who buzzed and got the right answer.
     * @return points to add
     */
    int calculatePoints(QuestionSession session, Player player);
}