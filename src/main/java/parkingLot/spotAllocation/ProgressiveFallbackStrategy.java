package parkingLot.spotAllocation;

import parkingLot.model.ParkingFloor;
import parkingLot.spot.ParkingSpot;
import parkingLot.vehicle.Vehicle;
import parkingLot.vehicle.VehicleType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class ProgressiveFallbackStrategy implements SpotAssignmentStrategy {
    private final List<VehicleType> sortedTiers = Arrays.stream(VehicleType.values())
            .sorted(Comparator.comparingInt(VehicleType::getSizeWeight))
            .collect(Collectors.toList());

    @Override
    public ParkingSpot findAndReserveSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            for (VehicleType currentTier : sortedTiers) {
                if (currentTier.getSizeWeight() >= vehicle.getType().getSizeWeight()) {
                    PriorityBlockingQueue<ParkingSpot> queue = floor.getFreeSpots(currentTier);

                    ParkingSpot spot = queue.poll();
                    while (spot != null) {
                        if (spot.assignVehicle(vehicle)) {
                            return spot;
                        } else {
                            queue.add(spot);
                            spot = queue.poll();
                        }
                    }
                }
            }
        }
        return null;
    }
}