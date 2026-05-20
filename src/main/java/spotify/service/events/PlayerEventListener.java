package spotify.service.events;

import spotify.models.Playable;

public interface PlayerEventListener {
    void onTrackStarted(Playable track);
    void onTrackSeeked(Playable track, int positionSeconds);
}