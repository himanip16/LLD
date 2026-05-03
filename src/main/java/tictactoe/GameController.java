package tictactoe;

import tictactoe.model.*;
import tictactoe.strategy.StandardWinningStrategy;
import tictactoe.strategy.WinningStrategy;

import java.util.*;

public class GameController {
    private final Deque<Player> players;
    private final Board board;
    private final WinningStrategy winningStrategy;
    private int availableCells;

    public GameController(List<Player> playerList, int boardSize) {
        this.players = new LinkedList<>(playerList);
        this.board = new Board(boardSize);
        this.winningStrategy = new StandardWinningStrategy();
        this.availableCells = boardSize * boardSize;
    }

    public void startGame() {
        boolean noWinner = true;
        Scanner scanner = new Scanner(System.in);

        while (noWinner) {
            board.printBoard();

            if (availableCells == 0) {
                System.out.println("It's a Tie!");
                break;
            }

            // 1. Get current player
            Player currentPlayer = players.pollFirst();
            System.out.println("Player " + currentPlayer.getName() + " (" + currentPlayer.getPlayingPiece().getPieceType() + "), enter row and column (e.g. 0 2): ");

            int inputRow = scanner.nextInt();
            int inputCol = scanner.nextInt();

            // 2. Try to add piece to board
            boolean moveValid = board.addPiece(inputRow, inputCol, currentPlayer.getPlayingPiece());
            if (!moveValid) {
                System.out.println("Invalid position! Please choose another cell.");
                players.addFirst(currentPlayer); // Re-add the player to try again
                continue;
            }

            availableCells--;
            players.addLast(currentPlayer); // Put player at the back of the queue

            // 3. Check for a winner
            boolean isWinner = winningStrategy.checkWinner(board, inputRow, inputCol, currentPlayer.getPlayingPiece().getPieceType());
            if (isWinner) {
                board.printBoard();
                System.out.println("Congratulations! Player " + currentPlayer.getName() + " has won the game!");
                noWinner = false;
            }
        }
    }
}