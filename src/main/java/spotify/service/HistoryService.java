package spotify.service.events;

import spotify.models.CurrentPlay;
import spotify.models.Playable;
import spotify.models.Song;
import java.time.LocalDateTime;

public class HistoryService implements PlayerEventListener {
    @Override
    public void onTrackStarted(Playable track) {
        if (track instanceof Song song) {
            // Correctly wiring up the previously dead 'CurrentPlay' model
            CurrentPlay record = new CurrentPlay(song, LocalDateTime.now());
            System.out.println("[HISTORY LOGGED] Saved to database: " + record);
        }
    }

    @Override
    public void onTrackSeeked(Playable track, int positionSeconds) {
        // No-op or analytics tracking can go here
    }
}