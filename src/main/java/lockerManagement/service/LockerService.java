package lockerManagement.service;

import lockerManagement.exception.NoSlotAvailableException;
import lockerManagement.exception.PackageNotFoundException;
import lockerManagement.model.DeliveryReceipt;
import lockerManagement.model.LockerSlot;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import lockerManagement.model.Package;
import lockerManagement.model.Size;

public class LockerService {

    // Map of size to a thread-safe Priority Queue
    private final Map<Size, PriorityBlockingQueue<LockerSlot>> availableSlotsPool;
    private final Map<String, LockerSlot> packageToSlotMap;
    private final Map<String, LockerSlot> idToSlotMap;
    private final PinGenerator pinService;

    public LockerService(List<LockerSlot> allSlots, PinGenerator pinService) {
        this.pinService = pinService;
        this.packageToSlotMap = new ConcurrentHashMap<>();
        this.idToSlotMap = new ConcurrentHashMap<>();
        this.availableSlotsPool = new ConcurrentHashMap<>();

        // Initialize queues for all possible sizes
        for (Size size : Size.values()) {
            // Sort by slot volume / capacity ascending (Smallest fitting locker first)
            this.availableSlotsPool.put(size, new PriorityBlockingQueue<>(11,
                    Comparator.comparingDouble(slot -> slot.volume)
            ));
        }

        // Populate pools
        for (LockerSlot slot : allSlots) {
            idToSlotMap.put(slot.slotId, slot);
            if (slot.isAvailable()) {
                availableSlotsPool.get(slot.size).offer(slot);
            }
        }
    }

    public DeliveryReceipt assignLocker(Package pkg) {
        Size requiredSize = pkg.size;
        LockerSlot allocatedSlot = null;

        // Cascade Upwards Strategy: If current size is full, try next larger size
        while (requiredSize != null && allocatedSlot == null) {
            PriorityBlockingQueue<LockerSlot> pq = availableSlotsPool.get(requiredSize);

            // poll() is atomic, preventing two threads from grabbing the exact same slot
            allocatedSlot = pq.poll();

            if (allocatedSlot == null) {
                requiredSize = requiredSize.next(); // Helper method on Enum
            }
        }

        if (allocatedSlot == null) {
            throw new NoSlotAvailableException("No available lockers fit package size: " + pkg.size);
        }

        // Fine-grained lock to safely update internal slot properties
        allocatedSlot.lock.lock();
        try {
            String pin = pinService.generate();
            allocatedSlot.reserve(pkg, pin);
            packageToSlotMap.put(pkg.packageId, allocatedSlot);
            return new DeliveryReceipt(allocatedSlot.slotId, pin);
        } finally {
            allocatedSlot.lock.unlock();
        }
    }

    public void pickupPackage(String packageId, String enteredPin) {
        LockerSlot slot = packageToSlotMap.get(packageId);
        if (slot == null) throw new PackageNotFoundException(packageId);

        slot.lock.lock();
        try {
            slot.pickup(enteredPin); // Validates PIN, changes internal state
            packageToSlotMap.remove(packageId);

            // Return back to the available pool
            availableSlotsPool.get(slot.size).offer(slot);
        } finally {
            slot.lock.unlock();
        }
    }
}