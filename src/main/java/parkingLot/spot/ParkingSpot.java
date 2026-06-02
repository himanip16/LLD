package parkingLot.spot;

import parkingLot.vehicle.SpotStatus;
import parkingLot.vehicle.Vehicle;
import parkingLot.vehicle.VehicleType;

public class ParkingSpot implements Comparable<ParkingSpot> {
    private final String spotId;
    private final int floorNumber;
    private final int distanceId;
    private final VehicleType spotType;
    private SpotStatus status = SpotStatus.AVAILABLE;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, int floorNumber, int distanceId, VehicleType spotType) {
        this.spotId = spotId;
        this.floorNumber = floorNumber;
        this.distanceId = distanceId;
        this.spotType = spotType;
    }

    @Override
    public int compareTo(ParkingSpot other) {
        return Integer.compare(this.distanceId, other.distanceId);
    }

    public synchronized boolean assignVehicle(Vehicle vehicle) {
        if (this.status == SpotStatus.AVAILABLE && this.spotType.getSizeWeight() >= vehicle.getType().getSizeWeight()) {
            this.parkedVehicle = vehicle;
            this.status = SpotStatus.OCCUPIED;
            return true;
        }
        return false;
    }

    public synchronized void removeVehicle() {
        this.parkedVehicle = null;
        this.status = SpotStatus.AVAILABLE;
    }

    public String getSpotId() { return spotId; }
    public int getFloorNumber() { return floorNumber; }
    public SpotStatus getStatus() { return status; }
    public VehicleType getSpotType() { return spotType; }
}