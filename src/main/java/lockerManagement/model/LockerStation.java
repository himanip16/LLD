package lockerManagement.model;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LockerStation {
    private final String stationId;
    private final String name;
    private final Location location;

    // Unified Single Source of Truth
    private final Map<String, Locker> lockerRegistry = new ConcurrentHashMap<>();
    private final Map<LockerSize, ConcurrentLinkedQueue<Locker>> availablePools = new EnumMap<>(LockerSize.class);

    private static final Map<LockerSize, List<LockerSize>> SIZE_HIERARCHY = new EnumMap<>(LockerSize.class);
    static {
        SIZE_HIERARCHY.put(LockerSize.S, Arrays.asList(LockerSize.S, LockerSize.M, LockerSize.L, LockerSize.XL));
        SIZE_HIERARCHY.put(LockerSize.M, Arrays.asList(LockerSize.M, LockerSize.L, LockerSize.XL));
        SIZE_HIERARCHY.put(LockerSize.L, Arrays.asList(LockerSize.L, LockerSize.XL));
        SIZE_HIERARCHY.put(LockerSize.XL, Arrays.asList(LockerSize.XL));
    }

    public LockerStation(String stationId, String name, Location location, List<Locker> initialLockers) {
        this.stationId = stationId;
        this.name = name;
        this.location = location;

        for (LockerSize size : LockerSize.values()) {
            availablePools.put(size, new ConcurrentLinkedQueue<>());
        }
        for (Locker locker : initialLockers) {
            lockerRegistry.put(locker.getId(), locker);
            if (locker.getState() == LockerState.AVAILABLE) {
                availablePools.get(locker.getSize()).add(locker);
            }
        }
    }

    /**
     * Fixes Concurrency Poll-Then-Lock Race Condition:
     * If a thread polls a locker but loses the lock racing sequence to another thread,
     * it continues looping to seek alternative lockers instead of crashing out.
     */
    public Locker acquireOptimalLocker(LockerSize packageSize) {
        List<LockerSize> candidateSizes = SIZE_HIERARCHY.get(packageSize);

        for (LockerSize size : candidateSizes) {
            ConcurrentLinkedQueue<Locker> queue = availablePools.get(size);
            Locker locker;

            while ((locker = queue.poll()) != null) {
                // Attempt lock acquisition instantly to protect mutation
                if (locker.getLock().tryLock()) {
                    try {
                        if (locker.getState() == LockerState.AVAILABLE) {
                            return locker; // Lock is held and passed out securely!
                        }
                    } finally {
                        // If it wasn't available, unlock immediately and continue looping
                        if (locker.getState() != LockerState.AVAILABLE) {
                            locker.getLock().unlock();
                        }
                    }
                }
                // If tryLock fails because someone else is holding it, keep moving through the pool
            }
        }
        return null;
    }

    public void returnToAvailablePool(Locker locker) {
        availablePools.get(locker.getSize()).add(locker);
    }

    public Locker getLocker(String lockerId) {
        return lockerRegistry.get(lockerId);
    }

    public Collection<Locker> getAllLockers() {
        return lockerRegistry.values();
    }

    public String getStationId() { return stationId; }
}
