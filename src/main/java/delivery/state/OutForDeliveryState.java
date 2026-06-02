//package delivery.state;
//
//import delivery.context.DeliveryWorkflow;
//
//public final class OutForDeliveryState extends BaseDeliveryState {
//    @Override
//    public String getStateName() { return "OUT_FOR_DELIVERY"; }
//
//    @Override
//    public void scanPackage(DeliveryWorkflow context, String barcode) {
//        if (!context.getPackageInfo().getPackageId().equals(barcode)) {
//            throw new IllegalArgumentException("Scanned barcode mismatch.");
//        }
//
//        if (context.getPackageInfo().isRequiresOtp()) {
//            context.transitionTo(context.getStateFactory().verificationTriggered(), "Barcode validation completed successfully.");
//        } else {
//            context.transitionTo(context.getStateFactory().scanned(), "Standard drop-off scan completed.");
//        }
//    }
//}