//package delivery.context;
//
//import delivery.domain.PackageDetails;
//import delivery.domain.RetryPolicy;
//import delivery.domain.StateTransitionEvent;
//import delivery.factory.StateFactory;
//import delivery.observer.DeadLetterQueue;
//import delivery.observer.WorkflowObserver;
//import delivery.service.OtpVerificationService;
//import delivery.service.PhotoStorageValidator;
//import delivery.state.DeliveryState;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public final class DeliveryWorkflow {
//    // Immutable safely-published dependencies (Thread-safe, no synchronization needed)
//    private final PackageDetails packageInfo;
//    private final OtpVerificationService otpService;
//    private final PhotoStorageValidator storageValidator;
//    private final StateFactory stateFactory;
//    private final RetryPolicy retryPolicy;
//    private final DeadLetterQueue dlq;
//
//    // Explicit internal monitors for distinct shared state mutations
//    private final Object stateLock = new Object();
//    private final List<WorkflowObserver> observers = new ArrayList<>();
//    private final List<StateTransitionEvent> auditHistory = new ArrayList<>();
//
//    // Mutable fields guarded strictly by stateLock
//    private DeliveryState currentState;
//    private int otpAttempts = 0;
//    private int deliveryRetryAttempts = 0;
//
//    public DeliveryWorkflow(PackageDetails packageInfo,
//                            OtpVerificationService otpService,
//                            PhotoStorageValidator storageValidator,
//                            StateFactory stateFactory,
//                            RetryPolicy retryPolicy,
//                            DeadLetterQueue dlq) {
//        this.packageInfo = packageInfo;
//        this.otpService = otpService;
//        this.storageValidator = storageValidator;
//        this.stateFactory = stateFactory;
//        this.retryPolicy = retryPolicy;
//        this.dlq = dlq;
//        this.currentState = stateFactory.outForDelivery();
//    }
//
//    public void registerObserver(WorkflowObserver observer) {
//        synchronized (stateLock) {
//            if (observer != null) observers.add(observer);
//        }
//    }
//
//    /**
//     * Atomically mutates the current state and returns a snapshot ledger of
//     * observers to be invoked completely free of any active locks.
//     */
//    public List<WorkflowObserver> changeState(DeliveryState newState, String metadata) {
//        List<WorkflowObserver> observerSnapshot;
//
//        synchronized (stateLock) {
//            StateTransitionEvent event = new StateTransitionEvent(
//                    packageInfo.getPackageId(),
//                    this.currentState.getStateName(),
//                    newState.getStateName(),
//                    metadata
//            );
//            this.auditHistory.add(event);
//            this.currentState = newState;
//
//            // Take an instantaneous snapshot clone of the registers
//            observerSnapshot = new ArrayList<>(this.observers);
//        } // Critical stateLock is released immediately here!
//
//        return observerSnapshot;
//    }
//
//    /**
//     * Dispatches state updates to observers safely outside critical thread locks.
//     */
//    private void dispatchStateChange(List<WorkflowObserver> snapshot, String stateName) {
//        for (WorkflowObserver observer : snapshot) {
//            try {
//                observer.onStateChange(packageInfo.getPackageId(), stateName, packageInfo);
//            } catch (Exception ex) {
//                dlq.captureFault(packageInfo.getPackageId(), observer.getClass().getName(), ex);
//            }
//        }
//    }
//
//    public void dispatchFailure(String action, String reason) {
//        List<WorkflowObserver> snapshot;
//        synchronized (stateLock) {
//            snapshot = new ArrayList<>(this.observers);
//        }
//        for (WorkflowObserver observer : snapshot) {
//            try {
//                observer.onActionFailure(packageInfo.getPackageId(), action, reason);
//            } catch (Exception ex) {
//                dlq.captureFault(packageInfo.getPackageId(), observer.getClass().getName(), ex);
//            }
//        }
//    }
//
//    // --- Unsynchronized Read Accessors (Safe publication via final references) ---
//    public PackageDetails getPackageInfo() { return packageInfo; }
//    public OtpVerificationService getOtpService() { return otpService; }
//    public PhotoStorageValidator getStorageValidator() { return storageValidator; }
//    public StateFactory getStateFactory() { return stateFactory; }
//    public RetryPolicy getRetryPolicy() { return retryPolicy; }
//
//    public String getCurrentStateName() {
//        synchronized (stateLock) {
//            return currentState.getStateName();
//        }
//    }
//
//    public List<StateTransitionEvent> getAuditHistory() {
//        synchronized (stateLock) {
//            return Collections.unmodifiableList(new ArrayList<>(this.auditHistory));
//        }
//    }
//
//    // --- Stateful Variable Mutators (Internal Protected Scope) ---
//    public int incrementOtpAttempts() {
//        synchronized (stateLock) { return ++this.otpAttempts; }
//    }
//
//    public int incrementDeliveryRetryAttempts() {
//        synchronized (stateLock) { return ++this.deliveryRetryAttempts; }
//    }
//
//    public int getDeliveryRetryAttempts() {
//        synchronized (stateLock) { return this.deliveryRetryAttempts; }
//    }
//
//    public void resetOtpAttempts() {
//        synchronized (stateLock) { this.otpAttempts = 0; }
//    }
//
//    // --- Client Processing Invocations: High Throughput Non-Blocking I/O Boundary ---
//
//    public void scanPackage(String barcode) {
//        DeliveryState cachedState;
//        synchronized (stateLock) {
//            cachedState = this.currentState;
//        }
//        // Business execution strategy runs independent of global thread stalls
//        cachedState.scanPackage(this, barcode);
//    }
//
//    public void verifyOtp(String otp) {
//        DeliveryState cachedState;
//        synchronized (stateLock) {
//            cachedState = this.currentState;
//        }
//        cachedState.verifyOtp(this, otp);
//    }
//
//    public void capturePhoto(String photoUrl) {
//        DeliveryState cachedState;
//        synchronized (stateLock) {
//            cachedState = this.currentState;
//        }
//        cachedState.capturePhoto(this, photoUrl);
//    }
//
//    public void triggerRetry() {
//        DeliveryState cachedState;
//        synchronized (stateLock) {
//            cachedState = this.currentState;
//        }
//        cachedState.triggerRetry(this);
//    }
//
//    // Package-private hook allowing state context consolidation execution
//    void executeTransition(DeliveryState targetState, String traceMessage) {
//        List<WorkflowObserver> notificationTargets = this.changeState(targetState, traceMessage);
//        this.dispatchStateChange(notificationTargets, targetState.getStateName());
//    }
//}