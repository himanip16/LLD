package lockerManagement.exception;

public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String packageId) {
        super("Package not found: " + packageId);
    }
}
