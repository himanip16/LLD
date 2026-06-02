package lockerManagement.model;

import java.util.concurrent.locks.ReentrantLock;

public class Locker {
    private final String id;
    private final LockerSize size;
    private final String stationId;
    private final ReentrantLock lock = new ReentrantLock();
    private LockerState state;
    private String currentPackageId;

    public Locker(String id, LockerSize size, String stationId) {
        this.id = id;
        this.size = size;
        this.stationId = stationId;
        this.state = LockerState.AVAILABLE;
    }

    public String getId() { return id; }
    public LockerSize getSize() { return size; }
    public String getStationId() { return stationId; }
    public ReentrantLock getLock() { return lock; }
    public LockerState getState() { return state; }
    public void setState(LockerState state) { this.state = state; }
    public String getCurrentPackageId() { return currentPackageId; }

    public void reserve(String packageId) {
        this.currentPackageId = packageId;
        this.state = LockerState.RESERVED;
    }

    public void allocatePackage() {
        this.state = LockerState.OCCUPIED;
    }

    public void clear() {
        this.currentPackageId = null;
        this.state = LockerState.AVAILABLE;
    }
}
