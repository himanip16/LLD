package spotify.service;

import spotify.models.Playable;
import spotify.models.Playlist;
import spotify.models.Song;
import spotify.service.events.PlayerEventListener;
import spotify.service.strategies.PlaybackStrategy;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final PlaybackNavigator navigator = new PlaybackNavigator();
    private final List<PlayerEventListener> listeners = new ArrayList<>();

    private Playable currentTrack;
    private int currentPositionSeconds = 0;

    public void addEventListener(PlayerEventListener listener) {
        listeners.add(listener);
    }

    public void loadPlaylist(Playlist playlist) {
        navigator.setPlaylist(playlist);
        if (playlist != null && !playlist.getSongs().isEmpty()) {
            currentTrack = playlist.getSongs().get(0);
        }
    }

    public void setPlaybackMode(PlaybackStrategy strategy) {
        navigator.setStrategy(strategy);
    }

    public void queue(Song song) {
        navigator.addToQueue(song);
    }

    public void play() {
        if (currentTrack == null) {
            System.out.println("No media loaded.");
            return;
        }
        System.out.println("▶️ Now Playing: " + currentTrack.getTitle() + " (" + currentTrack.getDurationSeconds() + "s)");

        // Notify tracking/history observers completely outside this class
        listeners.forEach(l -> l.onTrackStarted(currentTrack));
    }

    public void next() {
        // Playable conversion assumes safety or abstract navigation handling
        currentTrack = navigator.next((Song) currentTrack);
        currentPositionSeconds = 0;
        play();
    }

    public void prev() {
        currentTrack = navigator.prev();
        currentPositionSeconds = 0;
        play();
    }

    public void seekForward(int seconds) {
        if (currentTrack == null) return;
        currentPositionSeconds = Math.min(currentTrack.getDurationSeconds(), currentPositionSeconds + seconds);
        listeners.forEach(l -> l.onTrackSeeked(currentTrack, currentPositionSeconds));
        System.out.println("⏩ Seeked to " + currentPositionSeconds + "s");
    }
}