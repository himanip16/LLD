package lockerManagement.model;

public class Location {
    private final double latitude;
    private final double longitude;
    private final String geohash;

    public Location(double latitude, double longitude, String geohash) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geohash = geohash;
    }
    // Getters
}
