package spotify;

import spotify.models.*;
import spotify.service.Player;

public class SpotifyMain {

    public static void main(String[] args) {

        Singer kk = new Singer(1, "KK");
        Album album = new Album(1, "Tum Mile", kk);

        Song song1 = new Song(1, "Dil Ibadat", kk, album, 12300);
        Song song2 = new Song(2, "Khuda Jaane", kk, album, 12300);
        Song song3 = new Song(3, "Tu Hi Meri Shab Hai", kk, album, 123009);

        User user = new User(1, "Himani");

        Playlist playlist = new Playlist(1, user, "Favs");

        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);

        Player player = new Player();

        player.loadPlaylist(playlist);

        player.play();

        player.next();
        player.next();
        player.prev();

        playlist.shuffle();


    }
}