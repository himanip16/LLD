package atm.cash;

import java.util.HashMap;
import java.util.Map;

public class DispenseRequest {
    private final int totalAmount;
    private final Map<Integer, Integer> finalAllocationPlan = new HashMap<>();

    public DispenseRequest(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    public int getTotalAmount() { return totalAmount; }
    public Map<Integer, Integer> getAllocationPlan() { return finalAllocationPlan; }

    public void addAllocation(int denomination, int noteCount) {
        if (noteCount > 0) {
            finalAllocationPlan.put(denomination, noteCount);
        }
    }
}