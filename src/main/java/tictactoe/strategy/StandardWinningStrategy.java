package tictactoe.strategy;

import tictactoe.enums.PieceType;
import tictactoe.model.Board;
import tictactoe.model.PlayingPiece;

public class StandardWinningStrategy implements WinningStrategy {
    @Override
    public boolean checkWinner(Board board, int row, int col, PieceType pieceType) {
        PlayingPiece[][] grid = board.getGrid();
        int size = board.getSize();

        // 1. Check Row
        boolean rowMatch = true;
        for (int j = 0; j < size; j++) {
            if (grid[row][j] == null || grid[row][j].getPieceType() != pieceType) {
                rowMatch = false;
                break;
            }
        }
        if (rowMatch) return true;

        // 2. Check Column
        boolean colMatch = true;
        for (int i = 0; i < size; i++) {
            if (grid[i][col] == null || grid[i][col].getPieceType() != pieceType) {
                colMatch = false;
                break;
            }
        }
        if (colMatch) return true;

        // 3. Check employeeManagementService.Main Diagonal
        if (row == col) {
            boolean diagMatch = true;
            for (int i = 0; i < size; i++) {
                if (grid[i][i] == null || grid[i][i].getPieceType() != pieceType) {
                    diagMatch = false;
                    break;
                }
            }
            if (diagMatch) return true;
        }

        // 4. Check Anti-Diagonal
        if (row + col == size - 1) {
            boolean antiDiagMatch = true;
            for (int i = 0; i < size; i++) {
                if (grid[i][size - 1 - i] == null || grid[i][size - 1 - i].getPieceType() != pieceType) {
                    antiDiagMatch = false;
                    break;
                }
            }
            if (antiDiagMatch) return true;
        }

        return false;
    }
}
