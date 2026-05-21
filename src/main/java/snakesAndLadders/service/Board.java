package snakesAndLadders.service;

import snakesAndLadders.model.BoardSpecial;

import java.util.*;

public class Board {
    private final int totalCells;
    // Map storing special modifiers by their starting cell index
    private final Map<Integer, BoardSpecial> specials;

    public Board(int rows) {
        this.totalCells = rows * rows;
        this.specials = new HashMap<>();
    }

    public int getTotalCells() { return totalCells; }

    public void addSpecial(BoardSpecial special) {
        if (special.getStart() >= totalCells || special.getEnd() > totalCells) {
            throw new IllegalArgumentException("Special modifiers cannot exceed the boundaries of the board.");
        }
        specials.put(special.getStart(), special);
    }

    /**
     * Recursively resolves the final landing cell position.
     * Handles edge cases like chained modifiers (if explicitly permitted by configuration).
     */
    public int resolveTargetPosition(int position) {
        if (!specials.containsKey(position)) {
            return position;
        }
        BoardSpecial special = specials.get(position);
        System.out.print(special.getLogMessage() + special.getEnd());
        // Tail recursion allows cascading modifiers cleanly
        return resolveTargetPosition(special.getEnd());
    }
}
