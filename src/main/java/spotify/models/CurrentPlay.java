package spotify.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CurrentPlay {
    private int id;
    private User user;
    private Song song;
    private LocalDateTime timeStamp;


    public CurrentPlay(Song song, LocalDateTime now) {
        this.song = song;
        this.timeStamp = now;
    }
}
