package lockerManagement.slotAllocation;

import lockerManagement.model.LockerSlot;
import lockerManagement.model.Package;

import java.util.List;

public interface SlotAllocationStrategy {
    LockerSlot findSlot(List<LockerSlot> slots, Package pkg);
}
