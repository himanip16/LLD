package lockerManagement.slotAllocation;

import lockerManagement.model.LockerSlot;
import lockerManagement.model.Size;
import lockerManagement.model.Package;
import java.util.List;

public class BestFitStrategy implements SlotAllocationStrategy {

    @Override
    public LockerSlot findSlot(List<LockerSlot> slots, Package pkg) {
        Size current = pkg.size;

        while (current != null) {
            for (LockerSlot slot : slots) {
                if (slot.size == current && slot.isAvailable()) {
                    return slot;
                }
            }
            current = current.next();
        }

        return null;
    }
}