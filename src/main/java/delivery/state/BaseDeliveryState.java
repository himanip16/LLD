package delivery.state;

import delivery.context.DeliveryWorkflow;

/**
 * Abstract Null-Fallback class. Implements the Interface Segregation Principle
 * by defaulting all actions to throw an exception. Concrete states only override
 * the specific actions they structurally support.
 */
public abstract class BaseDeliveryState implements DeliveryState {

    @Override
    public void scanPackage(DeliveryWorkflow context, String barcode) {
        throw new IllegalStateException("Cannot scan package while in " + getStateName() + " state.");
    }

    @Override
    public void verifyOtp(DeliveryWorkflow context, String otp) {
        throw new IllegalStateException("Cannot verify OTP while in " + getStateName() + " state.");
    }

    @Override
    public void capturePhoto(DeliveryWorkflow context, String photoUrl) {
        throw new IllegalStateException("Cannot capture proof photo while in " + getStateName() + " state.");
    }

    @Override
    public void retryDelivery(DeliveryWorkflow context) {
        throw new IllegalStateException("Cannot trigger delivery retry while in " + getStateName() + " state.");
    }

    @Override
    public String toString() {
        return getStateName();
    }
}