//package delivery.state;
//
//import delivery.context.DeliveryWorkflow;
//
//public final class DeliveryFailedState extends BaseDeliveryState {
//    private final String reason;
//
//    public DeliveryFailedState(String reason) { this.reason = reason; }
//
//    @Override
//    public String getStateName() { return "DELIVERY_FAILED"; }
//
//    @Override
//    public void retryDelivery(DeliveryWorkflow context) {
//        context.resetOtpAttempts();
//        context.transitionTo(context.getStateFactory().outForDelivery(), "Operational clear signal received. Retry path active.");
//    }
//}