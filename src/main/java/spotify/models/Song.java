package spotify.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Song implements Playable {
    private final int id;
    private final String title;
    private final Singer artist;
    private final Album album;
    private final int durationSeconds;

    @Override
    public PlayableType getType() {
        return PlayableType.SONG;
    }

}
