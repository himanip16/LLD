package spotify.service;

import spotify.models.CurrentPlay;
import spotify.models.Playlist;
import spotify.models.Song;
import spotify.models.User;
import spotify.service.events.PlayerEventListener;
import spotify.service.strategies.PlaybackStrategy;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final PlaybackNavigator navigator = new PlaybackNavigator();
    private final List<PlayerEventListener> listeners = new ArrayList<>();

    private final User currentUser;
    private CurrentPlay activeSession; // Holds our rich cohesive runtime state

    public Player(User user) {
        this.currentUser = user;
    }

    public void addEventListener(PlayerEventListener listener) {
        listeners.add(listener);
    }

    private void createNewSession(Song song) {
        if (song == null) return;
        // Lifecycle Birth: Creating a brand new runtime identity bound to a static asset
        this.activeSession = new CurrentPlay(currentUser, song);
    }

    public void play() {
        if (activeSession == null) return;
        activeSession.setPaused(false);

        // Broadcast the entire rich domain snapshot out to observers
        listeners.forEach(l -> l.onPlaybackSessionStarted(activeSession));
    }

    public void pause() {
        if (activeSession == null) return;
        activeSession.setPaused(true);
        listeners.forEach(l -> l.onPlaybackSessionMutated(activeSession));
    }

    public void seekForward(int seconds) {
        if (activeSession == null) return;
        activeSession.seekTo(activeSession.getCurrentPositionSeconds() + seconds);

        // Observers can track exactly how far into the track the user seeked
        listeners.forEach(l -> l.onPlaybackSessionMutated(activeSession));
    }

    public void playSingle(Song song) {
        createNewSession(song);
        play();
    }

    public void next() {
        Song nextTrack = navigator.next(activeSession != null ? activeSession.getSong() : null);
        if (nextTrack != null) {
            createNewSession(nextTrack);
            play();
        }
    }

    public void loadPlaylist(Playlist playlist) {
        navigator.setPlaylist(playlist);
        if (playlist != null && !playlist.getSongs().isEmpty()) {
            createNewSession(playlist.getSongs().get(0));
        }
    }

    public void setPlaybackMode(PlaybackStrategy strategy) { navigator.setStrategy(strategy); }
    public void queue(Song song) { navigator.addToQueue(song); }
}