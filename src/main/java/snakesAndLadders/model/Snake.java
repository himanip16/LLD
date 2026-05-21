package snakesAndLadders.model;


import snakesAndLadders.enums.SpecialType;

public class Snake extends BoardSpecial {
    public Snake(int start, int end) {
        super(start, end, SpecialType.SNAKE);
        if (start <= end) throw new IllegalArgumentException("Snakes must drop players downward!");
    }

    @Override
    public String getLogMessage() {
        return " -> Oh no, swallowed by a Snake! Slid down to ";
    }
}
