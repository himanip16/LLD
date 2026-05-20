package spotify.service.strategies;

import spotify.models.Playlist;
import spotify.models.Song;

public interface PlaybackStrategy {
    Song getNext(Playlist playlist, int currentIndex);
    Song getPrev(Playlist playlist, int currentIndex);
}