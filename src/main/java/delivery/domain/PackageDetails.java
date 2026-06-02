package delivery.domain;

import java.util.regex.Pattern;

public final class PackageDetails {
    private final String packageId;
    private final String customerPhone;
    private final boolean requiresOtp;

    // E.164 phone validation pattern
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    public PackageDetails(String packageId, String customerPhone, boolean requiresOtp) {
        if (packageId == null || packageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Package ID cannot be null or empty.");
        }
        if (customerPhone == null || !PHONE_PATTERN.matcher(customerPhone).matches()) {
            throw new IllegalArgumentException("Invalid E.164 phone format: " + customerPhone);
        }
        this.packageId = packageId;
        this.customerPhone = customerPhone;
        this.requiresOtp = requiresOtp;
    }

    public String getPackageId() { return packageId; }
    public String getCustomerPhone() { return customerPhone; }
    public boolean isRequiresOtp() { return requiresOtp; }
}
