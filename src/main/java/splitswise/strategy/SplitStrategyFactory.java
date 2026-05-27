package splitswise.strategy;


import splitswise.model.SplitType;

public class SplitStrategyFactory {
    public static SplitStrategy getStrategy(SplitType splitType) {
        switch (splitType) {
            case EQUAL:
                return new EqualSplitStrategy();
            case PERCENTAGE:
                return new PercentageSplitStrategy();
            default:
                throw new IllegalArgumentException("Unknown Split Type");
        }
    }
}