package parkingLot.spot;

import parkingLot.vehicle.SpotStatus;
import parkingLot.vehicle.Vehicle;
import parkingLot.vehicle.VehicleType;

public class ParkingSpot {
    private final String spotId;
    private int floorNumber;
    private final VehicleType spotType;
    private SpotStatus status = SpotStatus.AVAILABLE;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, int floorNumber, VehicleType spotType) {
        this.spotId = spotId;
        this.floorNumber = floorNumber;
        this.spotType = spotType;
    }

    public ParkingSpot(String spotId, VehicleType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
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

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    public SpotStatus getStatus() { return status; }
    public VehicleType getSpotType() { return spotType; }
}