package snakesAndLadders;

import snakesAndLadders.model.Dice;
import snakesAndLadders.model.Ladder;
import snakesAndLadders.model.Player;
import snakesAndLadders.model.Snake;
import snakesAndLadders.service.GameEngine;
import snakesAndLadders.service.Board;

import java.util.Arrays;
import java.util.List;

public class SnakesAndLaddersMain {
    public static void main(String[] args) {
        System.out.println("=== Initializing Board Configuration ===\n");

        // 1. Create a 10x10 Board Layout (100 total cells)
        Board board = new Board(10);

        // 2. Configure Board Modifiers (Snakes drop players, Ladders boost players)
        try {
            // Adding Snakes (Start position must be greater than End position)
            board.addSpecial(new Snake(99, 54));
            board.addSpecial(new Snake(70, 31));
            board.addSpecial(new Snake(52, 29));
            board.addSpecial(new Snake(25, 6));

            // Adding Ladders (Start position must be less than End position)
            board.addSpecial(new Ladder(3, 38));
            board.addSpecial(new Ladder(11, 42));
            board.addSpecial(new Ladder(41, 82));
            board.addSpecial(new Ladder(65, 86));

            System.out.println("[SUCCESS] Board entities mapped successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Invalid modifier placement configuration: " + e.getMessage());
            return;
        }

        // 3. Initialize Game Players
        Player player1 = new Player("P1", "Alpha");
        Player player2 = new Player("P2", "Bravo");
        Player player3 = new Player("P3", "Charlie");
        List<Player> startingLineup = Arrays.asList(player1, player2, player3);

        // 4. Initialize Dice Component (Standard 1-Die Game Setup)
        Dice standardDie = new Dice(1);

        // 5. Construct Game Engine and Bootstrap Simulation Loop
        GameEngine gameSession = new GameEngine(board, standardDie, startingLineup);

        System.out.println("All players ready. Handing off execution context to the state engine...\n");
        gameSession.startGame();
    }
}
