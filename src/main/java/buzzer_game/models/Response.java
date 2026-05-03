package buzzer_game.models;

import lombok.Getter;

@Getter
public class Response {
    private final Player player;
    private final Option chosenOption;
    private final long timeTakenMs; // Exact time elapsed between buzzer and submission

    public Response(Player player, Option chosenOption, long timeTakenMs) {
        this.player = player;
        this.chosenOption = chosenOption;
        this.timeTakenMs = timeTakenMs;
    }
}