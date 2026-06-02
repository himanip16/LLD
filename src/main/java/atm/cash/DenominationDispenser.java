package atm.cash;

import java.util.Map;

public class DenominationDispenser extends CashDispenser {
    private final int denominationValue;
    private int availableNotesCount;

    public DenominationDispenser(int denominationValue, int initialNotes) {
        this.denominationValue = denominationValue;
        this.availableNotesCount = initialNotes;
    }

    @Override
    public void dispense(DispenseRequest request, int remainingAmount) {
        if (remainingAmount <= 0) return;

        int requiredNotes = remainingAmount / denominationValue;
        int notesToDispense = Math.min(requiredNotes, this.availableNotesCount);

        if (notesToDispense > 0) {
            request.addAllocation(denominationValue, notesToDispense);
            remainingAmount -= (notesToDispense * denominationValue);
        }

        if (remainingAmount > 0) {
            if (this.nextDispenser != null) {
                this.nextDispenser.dispense(request, remainingAmount);
            } else {
                throw new IllegalStateException("Insufficient specific denominations available to dispense exact amount.");
            }
        }
    }



    @Override
    public void commitDeduction(Map<Integer, Integer> plan) {
        if (plan.containsKey(denominationValue)) {
            this.availableNotesCount -= plan.get(denominationValue);
        }
        if (this.nextDispenser != null) {
            this.nextDispenser.commitDeduction(plan);
        }
    }
}
