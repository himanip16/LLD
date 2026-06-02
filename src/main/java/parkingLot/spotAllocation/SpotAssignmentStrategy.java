package parkingLot.spotAllocation;

import parkingLot.model.ParkingFloor;
import parkingLot.spot.ParkingSpot;
import parkingLot.vehicle.Vehicle;

import java.util.List;

public interface SpotAssignmentStrategy {
    ParkingSpot findAndReserveSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
