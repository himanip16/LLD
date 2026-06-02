package lockerManagement.model;

public class Package {
    public final String packageId;
    public final Size size;
    public final String customerId;

    public Package(String packageId, Size size, String customerId) {
        this.packageId = packageId;
        this.size = size;
        this.customerId = customerId;
    }
}
