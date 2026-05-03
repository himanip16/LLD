package buzzer_game.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {

    @EqualsAndHashCode.Include
    private final String playerId;

    private final String name;

    public Player(String playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{id='" + playerId + "', name='" + name + "'}";
    }
}
