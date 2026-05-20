package spotify.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Album {
    private int id;
    private String name;
    private Singer artist;


    public Album(int id, String name, Singer singer) {
        this.id = id;
        this.name = name;
        this.artist = singer;
    }
}
