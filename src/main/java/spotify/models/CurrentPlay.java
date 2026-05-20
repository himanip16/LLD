package spotify.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class CurrentPlay {
    private final String sessionId; // Swapped to UUID string for distributed safety
    private final User user;
    private final Song song;
    private final LocalDateTime startedAt;

    private int currentPositionSeconds;
    private boolean isPaused;

    public CurrentPlay(User user, Song song) {
        this.sessionId = UUID.randomUUID().toString(); // Safe across distributed node restarts
        this.user = user;
        this.song = song;
        this.startedAt = LocalDateTime.now();
        this.currentPositionSeconds = 0;
        this.isPaused = false;
    }

    // Encapsulated mutations guarding domain invariants
    public void seekTo(int seconds) {
        this.currentPositionSeconds = Math.max(0, Math.min(song.getDurationSeconds(), seconds));
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public User getUser() { return user; }
    public Song getSong() { return song; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public int getCurrentPositionSeconds() { return currentPositionSeconds; }
    public boolean isPaused() { return isPaused; }

    @Override
    public String toString() {
        return String.format("CurrentPlay[Session: %s... | User: %s | Song: '%s' | Position: %ds/%ds | State: %s]",
                sessionId.substring(0, 8), user.getName(), song.getTitle(),
                currentPositionSeconds, song.getDurationSeconds(), isPaused ? "⏸️" : "▶️");
    }
}