package snakesAndLadders.model;

import snakesAndLadders.enums.SpecialType;

public abstract class BoardSpecial {
    private final int start;
    private final int end;
    private final SpecialType type;

    public BoardSpecial(int start, int end, SpecialType type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public SpecialType getType() {
        return type;
    }

    public abstract String getLogMessage();
}
