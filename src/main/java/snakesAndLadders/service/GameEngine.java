package snakesAndLadders.service;

import snakesAndLadders.model.Dice;
import snakesAndLadders.model.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameEngine {
    private final Board board;
    private final Dice dice;
    private final Queue<Player> playersTurnQueue;
    private Player winner;

    public GameEngine(Board board, Dice dice, List<Player> startingPlayers) {
        this.board = board;
        this.dice = dice;
        this.playersTurnQueue = new LinkedList<>(startingPlayers);
        this.winner = null;
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public void startGame() {
        System.out.println("=== The Game Has Begun! ===");

        while (!isGameOver()) {
            // 1. Fetch current player (State Loop Management)
            Player currentPlayer = playersTurnQueue.poll();
            int currentPos = currentPlayer.getPosition();

            // 2. Roll Dice
            int rollValue = dice.roll();
            int nextCalculatedPos = currentPos + rollValue;

            System.out.print("Player [" + currentPlayer.getName() + "] rolled a " + rollValue + " | Moving " + currentPos + " -> " + nextCalculatedPos);

            // 3. Rule Verification (Exact Landing Condition)
            if (nextCalculatedPos > board.getTotalCells()) {
                System.out.println(" -> Over-shot board limits! Turn skipped.");
                playersTurnQueue.add(currentPlayer); // Put back into the rotation loop
                continue;
            }

            // 4. Resolve Snakes or Ladders interactions
            int finalLandingPos = board.resolveTargetPosition(nextCalculatedPos);
            currentPlayer.setPosition(finalLandingPos);
            System.out.println();

            // 5. Check Winning Condition
            if (finalLandingPos == board.getTotalCells()) {
                this.winner = currentPlayer;
                System.out.println("\n🎉🏆 STAGE CLEARED! Player [" + currentPlayer.getName() + "] wins the game! 🏆🎉");
                break;
            }

            // 6. Return back to structural execution pipeline
            playersTurnQueue.add(currentPlayer);
        }
    }
}
