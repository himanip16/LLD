package atm.cash;

import java.util.Map;

public abstract class CashDispenser {
    protected CashDispenser nextDispenser;

    public void setNextDispenser(CashDispenser nextDispenser) {
        this.nextDispenser = nextDispenser;
    }
    public abstract void dispense(DispenseRequest request, int remainingAmount);
    public abstract void commitDeduction(Map<Integer, Integer> plan);
}