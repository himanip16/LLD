package tictactoe;

import tictactoe.model.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("        WELCOME TO TIC TAC TOE           ");
        System.out.println("==========================================");

        Player player1 = new Player("Anya", new PieceX());
        Player player2 = new Player("Rohan", new PieceO());

        GameController game = new GameController(Arrays.asList(player1, player2), 3);
        game.startGame();
    }
}