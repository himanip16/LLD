package buzzer_game.leaderBoard;

import buzzer_game.models.ScoreEvent;

public interface Leaderboard {
    /**
     * Responds to a new score event in real time.
     */
    void onScoreUpdate(ScoreEvent event);

    /**
     * Displays the current standings for its scope.
     */
    void display();
}