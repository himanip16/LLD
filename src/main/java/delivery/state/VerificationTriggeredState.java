package delivery.state;

import delivery.context.DeliveryWorkflow;

public final class VerificationTriggeredState extends BaseDeliveryState {
    @Override
    public String getStateName() { return "VERIFICATION_TRIGGERED"; }

    @Override
    public void verifyOtp(DeliveryWorkflow context, String otp) {
        boolean isValid = context.getOtpService().validateOtp(context.getPackageInfo().getPackageId(), otp);

        if (isValid) {
            context.transitionTo(context.getStateFactory().scanned(), "OTP matches security registry. Proceeding to visual proof capture.");
        } else {
            int attempts = context.incrementOtpAttempts();
            context.notifyFailure("VERIFY_OTP", "Incorrect entry payload. Evaluation counter: " + attempts);

            if (attempts >= 3) {
                context.notifyFailure("VERIFY_OTP", "Security threshold breach. Freezing transit sequence.");
                context.transitionTo(context.getStateFactory().deliveryFailed("EXCEEDED_OTP_RETRIES"), "Transferred out of active workflow due to repeated failures.");
            }
        }
    }
}