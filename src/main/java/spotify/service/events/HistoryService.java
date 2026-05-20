package spotify.service.events;

import spotify.models.CurrentPlay;
import java.util.ArrayList;
import java.util.List;

public class HistoryService implements PlayerEventListener {
    private final List<CurrentPlay> historicalArchive = new ArrayList<>();

    @Override
    public void onPlaybackSessionStarted(CurrentPlay session) {
        System.out.println("[📊 ANALYTICS START] User '" + session.getUser().getName() +
                "' initialized Session " + session.getSessionId().substring(0,8) +
                " for track: " + session.getSong().getTitle());
        historicalArchive.add(session);
    }

    @Override
    public void onPlaybackSessionMutated(CurrentPlay session) {
        System.out.println("[📈 TELEMETRY MUTATION] Session " + session.getSessionId().substring(0,8) +
                " changed state. Current Playhead: " + session.getCurrentPositionSeconds() + "s" +
                " | Paused: " + session.isPaused());
    }
}