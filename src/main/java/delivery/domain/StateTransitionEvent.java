package delivery.domain;

import java.time.Instant;

public final class StateTransitionEvent {
    private final String packageId;
    private final String fromState;
    private final String toState;
    private final Instant timestamp;
    private final String metadata;

    public StateTransitionEvent(String packageId, String fromState, String toState, String metadata) {
        this.packageId = packageId;
        this.fromState = fromState;
        this.toState = toState;
        this.timestamp = Instant.now();
        this.metadata = metadata;
    }

    public String getPackageId() { return packageId; }
    public String getFromState() { return fromState; }
    public String getToState() { return toState; }
    public Instant getTimestamp() { return timestamp; }
    public String getMetadata() { return metadata; }
}