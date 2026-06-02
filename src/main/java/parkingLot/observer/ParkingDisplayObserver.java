package parkingLot.observer;

import parkingLot.vehicle.VehicleType;

public interface ParkingDisplayObserver {
    void updateCapacity(VehicleType type, int newCount);
}