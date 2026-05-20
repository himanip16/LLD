package spotify.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class Playlist {
    private int id;
    private User user;
    private List<Song> songs;
    private String name;
    private int currentIndex;

     public Playlist(int id, User user, String name) {
        this.id = id;
        this.user = user;
        this.songs = new ArrayList<>();
        this.name = name;
    }

    public void add(Song song1) {
        songs.add(song1);
    }


        public Song getCurrentSong() {
            if (songs.isEmpty()) {
                return null;
            }
            return songs.get(currentIndex);
        }

        public void next() {
            if (songs.isEmpty()) return;

            // Loop back to the beginning if we reach the end
            currentIndex = (currentIndex + 1) % songs.size();
            System.out.println("Playing next: " + getCurrentSong().getTitle());
        }

        public void prev() {
            if (songs.isEmpty()) return;

            // Loop to the end if we go back past the first song
            currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
            System.out.println("Playing previous: " + getCurrentSong().getTitle());
        }

        public void shuffle() {
            if (songs.isEmpty()) return;

            // Optional: Save the current song to find its new index after shuffling
            Song current = getCurrentSong();
            Collections.shuffle(songs);

            // Reset currentIndex to wherever our current song landed
            if (current != null) {
                currentIndex = songs.indexOf(current);
            }
            System.out.println("Playlist shuffled!");
        }
    }