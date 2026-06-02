package parkingLot.spot;

import parkingLot.vehicle.VehicleType;

class LargeSpot extends ParkingSpot {
    public LargeSpot(String id) {
        super(id,  VehicleType.TRUCK);
    }
}
