package snakesAndLadders.model;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    private final int numberOfDice;
    private static final int MAX_FACE = 6;

    public Dice(int numberOfDice) {
        if (numberOfDice < 1) throw new IllegalArgumentException("Must have at least 1 die.");
        this.numberOfDice = numberOfDice;
    }

    public int roll() {
        int totalRoll = 0;
        for (int i = 0; i < numberOfDice; i++) {
            totalRoll += ThreadLocalRandom.current().nextInt(1, MAX_FACE + 1);
        }
        return totalRoll;
    }
}
