package delivery.state;

import delivery.context.DeliveryWorkflow;

/**
 * Top-level State interface defining all possible runner actions.
 */
public interface DeliveryState {
    void scanPackage(DeliveryWorkflow context, String barcode);
    void verifyOtp(DeliveryWorkflow context, String otp);
    void capturePhoto(DeliveryWorkflow context, String photoUrl);
    void retryDelivery(DeliveryWorkflow context);
    String getStateName();
}