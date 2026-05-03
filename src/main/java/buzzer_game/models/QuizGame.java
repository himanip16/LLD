package buzzer_game.models;

import buzzer_game.enums.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class QuizGame {
    private final String id;
    private final List<GameRound> gameRounds;
    private final List<Player> players = new CopyOnWriteArrayList<>();
    private final ScoreCard scoreCard;
    private volatile GameStatus status;

    private int currentRoundIndex = 0;
    private int currentQuestionIndex = 0;

    public QuizGame(List<GameRound> rounds) {
        this.id = UUID.randomUUID().toString();
        this.gameRounds = rounds;
        this.scoreCard = new ScoreCard(this.id);
        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }

    /**
     * Retrieves the current round safely.
     */
    public synchronized GameRound getCurrentRound() {
        if (currentRoundIndex < gameRounds.size()) {
            return gameRounds.get(currentRoundIndex);
        }
        return null; // Game is completed
    }

    /**
     * Retrieves the active question session within the current round.
     */
    public synchronized QuestionSession getActiveQuestionSession() {
        GameRound round = getCurrentRound();
        if (round != null && currentQuestionIndex < round.getQuestionSessions().size()) {
            return round.getQuestionSessions().get(currentQuestionIndex);
        }
        return null;
    }

    /**
     * Advances the internal pointers to the next question.
     * Moves to the next round automatically if questions are exhausted.
     */
    public synchronized void advanceToNextQuestion() {
        GameRound round = getCurrentRound();
        if (round == null) return;

        currentQuestionIndex++;
        // If we exhausted all questions in the current round, advance to the next round
        if (currentQuestionIndex >= round.getQuestionSessions().size()) {
            currentRoundIndex++;
            currentQuestionIndex = 0; // Reset pointer for new round
        }
    }

    // --- ScoreCard & Player Encapsulation ---

    public void registerPlayer(Player player) {
        this.scoreCard.initPlayer(player);
    }

    public void addScore(Player player, int points) {
        this.scoreCard.addScore(player, points);
    }

    public Map<Player, Integer> getScores() {
        return scoreCard.getPlayerScores();
    }
}