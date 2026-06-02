package parkingLot.service;

import parkingLot.model.ParkingFloor;
import parkingLot.observer.ParkingDisplayObserver;
import parkingLot.vehicle.VehicleType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLot {
    private final List<ParkingFloor> floors = new CopyOnWriteArrayList<>();
    private final List<ParkingDisplayObserver> observers = new CopyOnWriteArrayList<>();

    // Global tracking counter maps to maintain constant time O(1) display lookups
    private final Map<VehicleType, AtomicInteger> availableCounts = new ConcurrentHashMap<>();

    public ParkingLot() {
        for (VehicleType type : VehicleType.values()) {
            availableCounts.put(type, new AtomicInteger(0));
        }
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
        for (VehicleType type : VehicleType.values()) {
            int count = floor.getFreeSpots(type).size();
            availableCounts.get(type).addAndGet(count);
        }
    }

    public List<ParkingFloor> getFloors() { return floors; }

    public void subscribeObserver(ParkingDisplayObserver observer) { observers.add(observer); }

    public void incrementCapacity(VehicleType type) {
        int newCount = availableCounts.get(type).incrementAndGet();
        notifyObservers(type, newCount);
    }

    public void decrementCapacity(VehicleType type) {
        int newCount = availableCounts.get(type).decrementAndGet();
        notifyObservers(type, newCount);
    }

    private void notifyObservers(VehicleType type, int newCount) {
        for (ParkingDisplayObserver observer : observers) {
            observer.updateCapacity(type, newCount);
        }
    }
}