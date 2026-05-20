package spotify.service.events;

import spotify.models.CurrentPlay;

public interface PlayerEventListener {
    // Events now emit rich, contextual domain snapshots
    void onPlaybackSessionStarted(CurrentPlay session);
    void onPlaybackSessionMutated(CurrentPlay session);
}