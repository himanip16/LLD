package atm.cash;

import java.util.Map;

// Orchestrator for Cash Vault Operations
public class CashInventory {
    private final CashDispenser chainHead;

    public CashInventory() {
        // Construct Chain: 500 -> 200 -> 100
        DenominationDispenser d500 = new DenominationDispenser(500, 10);
        DenominationDispenser d200 = new DenominationDispenser(200, 20);
        DenominationDispenser d100 = new DenominationDispenser(100, 50);

        d500.setNextDispenser(d200);
        d200.setNextDispenser(d100);
        this.chainHead = d500;
    }

    // Atomic dry-run followed by operational hardware state updates
    public synchronized Map<Integer, Integer> allocateAndDeductCash(int amount) {
        if (amount % 100 != 0) {
            throw new IllegalArgumentException("Amount must be a multiple of 100.");
        }
        DispenseRequest request = new DispenseRequest(amount);

        // 1. Dry run validation stage (throws if unable to fulfill match)
        chainHead.dispense(request, amount);

        // 2. Commit internal structural inventory modification state
        Map<Integer, Integer> allocationPlan = request.getAllocationPlan();
        chainHead.commitDeduction(allocationPlan);

        return allocationPlan;
    }
}
