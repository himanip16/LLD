package spotify.service;

import spotify.models.Playlist;
import spotify.models.Song;
import spotify.service.strategies.PlaybackStrategy;
import spotify.service.strategies.SequentialStrategy;

import java.util.LinkedList;

public class PlaybackNavigator {
    private Playlist currentPlaylist;
    private int playlistIndex = 0;
    private final LinkedList<Song> customQueue = new LinkedList<>();
    private final LinkedList<Song> historyStack = new LinkedList<>(); // Fixes the missing prev() queue bug
    private PlaybackStrategy strategy = new SequentialStrategy();

    public void setPlaylist(Playlist playlist) {
        this.currentPlaylist = playlist;
        this.playlistIndex = 0;
    }

    public void setStrategy(PlaybackStrategy strategy) {
        this.strategy = strategy;
    }

    public void addToQueue(Song song) {
        customQueue.addLast(song);
    }

    public Song next(Song currentTrack) {
        if (currentTrack != null) {
            historyStack.push(currentTrack);
        }

        // 1. Drain manual user queue first
        if (!customQueue.isEmpty()) {
            return customQueue.pollFirst();
        }

        // 2. Otherwise fall back to playlist strategy context
        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) return null;

        Song nextTrack = strategy.getNext(currentPlaylist, playlistIndex);
        playlistIndex = currentPlaylist.getSongs().indexOf(nextTrack);
        return nextTrack;
    }

    public Song prev() {
        // 1. If we have track history, trace backwards
        if (!historyStack.isEmpty()) {
            return historyStack.pop();
        }

        // 2. Fall back to navigating the active playlist backwards
        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) return null;

        Song prevTrack = strategy.getPrev(currentPlaylist, playlistIndex);
        playlistIndex = currentPlaylist.getSongs().indexOf(prevTrack);
        return prevTrack;
    }
}