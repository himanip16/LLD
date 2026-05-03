package tictactoe.model;

import tictactoe.enums.PieceType;

public class PlayingPiece {
    private final PieceType pieceType;

    public PlayingPiece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}
