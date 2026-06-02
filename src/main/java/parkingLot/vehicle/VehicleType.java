package parkingLot.vehicle;

public enum VehicleType {
    MOTORBIKE(1), CAR(2), TRUCK(3);

    // Storing weights helps determine sizing order (Motorbike can fit in Car/Truck spaces)
    private final int sizeWeight;
    VehicleType(int weight) { this.sizeWeight = weight; }
    public int getSizeWeight() { return sizeWeight; }
}