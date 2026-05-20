package spotify.models;

public interface Playable {
    int getId();
    String getTitle();
    int getDurationSeconds();
    PlayableType getType();
}
