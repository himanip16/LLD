package buzzer_game.score;

import buzzer_game.models.Player;
import buzzer_game.models.QuestionSession;
import buzzer_game.score.ScoreStrategy;

public class FixedScoreStrategy implements ScoreStrategy {
    private final int pointsPerCorrectAnswer;

    public FixedScoreStrategy(int pointsPerCorrectAnswer) {
        this.pointsPerCorrectAnswer = pointsPerCorrectAnswer;
    }

    @Override
    public int calculatePoints(QuestionSession session, Player player) {
        return pointsPerCorrectAnswer;
    }
}