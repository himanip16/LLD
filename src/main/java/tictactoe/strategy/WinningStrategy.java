package tictactoe.strategy;

import tictactoe.model.Board;
import tictactoe.enums.PieceType;

public interface WinningStrategy {
    boolean checkWinner(Board board, int row, int col, PieceType pieceType);
}

