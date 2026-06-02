//package delivery.state;
//
//import delivery.context.DeliveryWorkflow;
//
//public final class ScannedState extends BaseDeliveryState {
//    @Override
//    public String getStateName() { return "SCANNED"; }
//
//    @Override
//    public void capturePhoto(DeliveryWorkflow context, String photoUrl) {
//        if (photoUrl == null || !photoUrl.startsWith("https://")) {
//            context.notifyFailure("CAPTURE_PHOTO", "Unsecured target storage bucket URL.");
//            throw new IllegalArgumentException("Secure cloud storage location required.");
//        }
//        context.transitionTo(context.getStateFactory().delivered(), "Visual signature captured: " + photoUrl);
//    }
//}