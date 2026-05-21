package snakesAndLadders.model;


import snakesAndLadders.enums.SpecialType;

public class Ladder extends BoardSpecial {
    public Ladder(int start, int end) {
        super(start, end, SpecialType.LADDER);
        if (start >= end) throw new IllegalArgumentException("Ladders must boost players upward!");
    }

    @Override
    public String getLogMessage() {
        return " -> Wow! Climbed a Ladder to ";
    }
}
