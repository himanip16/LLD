package spotify.service.strategies;

import spotify.models.Playlist;
import spotify.models.Song;

import java.util.Random;

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
