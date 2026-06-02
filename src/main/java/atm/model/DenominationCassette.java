package atm.model;

public class DenominationCassette {
    private final int noteValue;
    private int availableNotes;

    public DenominationCassette(int noteValue, int initialNotes) {
        this.noteValue = noteValue;
        this.availableNotes = initialNotes;
    }

    public int getNoteValue() { return noteValue; }
    public int getAvailableNotes() { return availableNotes; }
    public void deductNotes(int count) { this.availableNotes -= count; }
}

