package spotify.service.strategies;

import spotify.models.Playlist;
import spotify.models.Song;

public class SequentialStrategy implements PlaybackStrategy {
    @Override
    public Song getNext(Playlist playlist, int currentIndex) {
        if (playlist.getSongs().isEmpty()) return null;
        int nextIndex = (currentIndex + 1) % playlist.getSongs().size();
        return playlist.getSongs().get(nextIndex);
    }

    @Override
    public Song getPrev(Playlist playlist, int currentIndex) {
        if (playlist.getSongs().isEmpty()) return null;
        int prevIndex = (currentIndex - 1 + playlist.getSongs().size()) % playlist.getSongs().size();
        return playlist.getSongs().get(prevIndex);
    }
}

