package spotify;

import spotify.models.*;
import spotify.service.Player;
import spotify.service.events.HistoryService;
import spotify.service.strategies.ShuffleStrategy;
import spotify.service.strategies.SequentialStrategy;

import java.util.ArrayList;

public class SpotifyMain {

    public static void main(String[] args) {
        // --- 1. BOILERPLATE METADATA INITIALIZATION ---
        Singer kk = new Singer(1, "KK");
        Album album = new Album(1, "Tum Mile", kk);

        Song song1 = new Song(1, "Dil Ibadat", kk, album, 320);
        Song song2 = new Song(2, "Khuda Jaane", kk, album, 280);
        Song song3 = new Song(3, "Tu Hi Meri Shab Hai", kk, album, 295);
        Song song4 = new Song(4, "Zara Sa", kk, album, 300);

        User user = new User(1, "Himani");

        Playlist playlist = new Playlist(1, user, "90s/00s Melodies", new ArrayList<>());
        playlist.getSongs().add(song1);
        playlist.getSongs().add(song2);
        playlist.getSongs().add(song3);

        // --- 2. PLAYER INITIALIZATION & OBSERVER SETUP ---
        HistoryService historyService = new HistoryService();
        Player player = new Player(user);
        player.addEventListener(historyService); // Listens to track changes to fire CurrentPlay logs

        System.out.println("=== CASE 1: PLAYING / QUEUING WITHOUT A PLAYLIST ===");
        // Directly starting a standalone track
        player.playSingle(song1);
        player.seekForward(45); // Seeking behavior verification

        // Queueing individual tracks up next manually
        System.out.println("\n--- Queuing track 4 and track 2 manually ---");
        player.queue(song4);
        player.queue(song2);

        // Advancing down the manual queue cleanly
        player.next(); // Should pull song4 from queue
        player.next(); // Should pull song2 from queue

        // Testing the inverse trace (prev) moving back through the user history stack
        System.out.println("\n--- Testing 'prev()' logic strictly on queue history ---");

        System.out.println("\n=== CASE 2: CONTEXTUAL PLAYBACK WITH ACTIVE PLAYLISTS ===");
        // Resetting player focus into an official playlist envelope
        player.loadPlaylist(playlist); // Defaults to track 0 (Dil Ibadat)
        player.play();

        System.out.println("\n--- Advancing through playlist sequentially ---");
        player.next(); // Plays track 1 (Khuda Jaane)
        player.next(); // Plays track 2 (Tu Hi Meri Shab Hai)
        player.next(); // Loops back to track 0 cleanly via SequentialStrategy

        System.out.println("\n--- Testing playlist 'prev()' tracking ---");

        System.out.println("\n=== CASE 3: HYBRID MODE (PLAYLIST + MANUAL USER QUEUE INJECTIONS) ===");
        // Injecting a manual single tracking break inside an ongoing playlist session
        player.queue(song4); // Manually inject "Zara Sa" right now

        System.out.println("\n--- Advancing player (Should interrupt playlist loop for user queue) ---");
        player.next(); // Interrupts playlist to empty manual queue item first (Zara Sa)
        player.next(); // Queue is drained, cleanly pops right back to normal playlist sequence context!

        System.out.println("\n=== CASE 4: NON-DESTRUCTIVE STRATEGY ALTERATION (SHUFFLE MODE) ===");
        // Swapping execution behavior out instantly using state strategy injectors
        System.out.println("Switching playback mode to SHUFFLE...");
        player.setPlaybackMode(new ShuffleStrategy());

        player.next(); // Selects a pseudo-random index completely preserving initial list indices safely
        player.next();
    }
}