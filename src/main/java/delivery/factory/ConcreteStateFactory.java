//package delivery.factory;
//
//import delivery.state.*;
//
//// Thread-safe Singleton Factory keeping State objects purely stateless
//public final class ConcreteStateFactory implements StateFactory {
//    private static final DeliveryState OUT_FOR_DELIVERY = new OutForDeliveryState();
//    private static final DeliveryState VERIFICATION_TRIGGERED = new VerificationTriggeredState();
//    private static final DeliveryState SCANNED = new ScannedState();
//    private static final DeliveryState DELIVERED = new DeliveredState();
//
//    @Override
//    public DeliveryState outForDelivery() {
//        return OUT_FOR_DELIVERY;
//    }
//
//    @Override
//    public DeliveryState verificationTriggered() {
//        return VERIFICATION_TRIGGERED;
//    }
//
//    @Override
//    public DeliveryState scanned() {
//        return SCANNED;
//    }
//
//    @Override
//    public DeliveryState deliveryFailed(String reason) {
//        return new DeliveryFailedState(reason); // Dynamic context variance requires unique instantiation
//    }
//
//    @Override
//    public DeliveryState delivered() {
//        return DELIVERED;
//    }
//}
