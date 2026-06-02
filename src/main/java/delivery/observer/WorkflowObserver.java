package delivery.observer;

import delivery.domain.PackageDetails;

/**
 * Interface used to decouple tracking, metrics, notifications, and
 * logging systems from core business transition loops.
 */
public interface WorkflowObserver {
    void onStateChange(String packageId, String newState, PackageDetails details);
    void onActionFailure(String packageId, String action, String reason);
}