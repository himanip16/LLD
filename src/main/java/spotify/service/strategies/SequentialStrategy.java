package spotify.service.strategies;

import spotify.models.Playlist;
import spotify.models.Song;
import java.util.Random;

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

public class ShuffleStrategy implements PlaybackStrategy {
    private final Random random = new Random();

    @Override
    public Song getNext(Playlist playlist, int currentIndex) {
        if (playlist.getSongs().isEmpty()) return null;
        // In production, you'd maintain a history of played indices to avoid immediate repeats
        return playlist.getSongs().get(random.nextInt(playlist.getSongs().size()));
    }

    @Override
    public Song getPrev(Playlist playlist, int currentIndex) {
        if (playlist.getSongs().isEmpty()) return null;
        return playlist.getSongs().get(random.nextInt(playlist.getSongs().size()));
    }
}