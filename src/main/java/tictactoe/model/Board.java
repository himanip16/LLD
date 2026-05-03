package tictactoe.model;

public class Board {
    private final int size;
    private final PlayingPiece[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new PlayingPiece[size][size];
    }

    public boolean addPiece(int row, int col, PlayingPiece playingPiece) {
        if (row < 0 || row >= size || col < 0 || col >= size || grid[row][col] != null) {
            return false;
        }
        grid[row][col] = playingPiece;
        return true;
    }

    public void printBoard() {
        System.out.println("\n-------------");
        for (int i = 0; i < size; i++) {
            System.out.print("| ");
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != null) {
                    System.out.print(grid[i][j].getPieceType() + " | ");
                } else {
                    System.out.print("  | ");
                }
            }
            System.out.println("\n-------------");
        }
    }

    public PlayingPiece[][] getGrid() { return grid; }
    public int getSize() { return size; }
}