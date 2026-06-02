package lockerManagement.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class LockerToken {
    private final String tokenId;
    private final String lockerId;
    private final String stationId;
    private final String packageId;
    private final String secureCode;
    private final LocalDateTime expiryTime;
    private final AtomicBoolean isRedeemed = new AtomicBoolean(false);

    private LockerToken(Builder builder) {
        this.tokenId = builder.tokenId;
        this.lockerId = builder.lockerId;
        this.stationId = builder.stationId;
        this.packageId = builder.packageId;
        this.secureCode = builder.secureCode;
        this.expiryTime = builder.expiryTime;
    }

    public boolean isValid(String code) {
        return !isRedeemed.get() && LocalDateTime.now().isBefore(expiryTime) && this.secureCode.equals(code);
    }

    // Atomic Compare-And-Set to eliminate check-then-act races
    public boolean attemptRedemption() {
        return isRedeemed.compareAndSet(false, true);
    }

    public boolean isExpired() {
        return !isRedeemed.get() && LocalDateTime.now().isAfter(expiryTime);
    }

    public String getLockerId() { return lockerId; }
    public String getStationId() { return stationId; }
    public String getTokenId() { return tokenId; }
    public String getSecureCode() { return secureCode; }
    public String getPackageId() { return packageId; }

    public static class Builder {
        private String tokenId;
        private String lockerId;
        private String stationId;
        private String packageId;
        private String secureCode;
        private LocalDateTime expiryTime;

        public Builder tokenId(String tokenId) { this.tokenId = tokenId; return this; }
        public Builder lockerId(String lockerId) { this.lockerId = lockerId; return this; }
        public Builder stationId(String stationId) { this.stationId = stationId; return this; }
        public Builder packageId(String packageId) { this.packageId = packageId; return this; }
        public Builder secureCode(String secureCode) { this.secureCode = secureCode; return this; }
        public Builder expiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; return this; }

        public LockerToken build() {
            return new LockerToken(this);
        }
    }
}
