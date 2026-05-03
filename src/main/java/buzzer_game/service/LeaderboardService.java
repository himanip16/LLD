package buzzer_game.service;

import buzzer_game.leaderBoard.Leaderboard;
import buzzer_game.models.*;
import buzzer_game.score.ScoreStrategy;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardService {
    private final ScoreStrategy scoreStrategy;
    private final List<Leaderboard> subscribers = new ArrayList<>();

    public LeaderboardService(ScoreStrategy scoreStrategy) {
        this.scoreStrategy = scoreStrategy;
    }

    /**
     * Registers a specialized leaderboard (Round, Game, Global) to listen to score events.
     */
    public void registerLeaderboard(Leaderboard leaderboard) {
        subscribers.add(leaderboard);
    }

    /**
     * Calculates score upon a correct answer, updates the game's ScoreCard,
     * and broadcasts a ScoreEvent to all registered leaderboard subscribers.
     */
    public void addScoreForCorrectAnswer(QuizGame game, QuestionSession session, Player player) {
        int pointsAwarded = scoreStrategy.calculatePoints(session, player);

        if (pointsAwarded > 0) {
            // Update the isolated game scorecard directly
            game.addScore(player, pointsAwarded);

            // Create the score event for our real-time subscribers
            ScoreEvent event = new ScoreEvent(
                    game.getId(),
                    game.getCurrentRound().getRoundId(),
                    player,
                    pointsAwarded
            );

            // Broadcast the event using the food_delivery_system.observer.Observer pattern
            for (Leaderboard sub : subscribers) {
                sub.onScoreUpdate(event);
            }
        }
    }
}