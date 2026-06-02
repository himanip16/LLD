package parkingLot.spot;

import parkingLot.vehicle.VehicleType;

class CompactSpot extends ParkingSpot {
    public CompactSpot(String id) {
        super(id,  VehicleType.CAR);
    }
}
