package delivery.factory;

import delivery.state.*;

public interface StateFactory {
    DeliveryState outForDelivery();
    DeliveryState verificationTriggered();
    DeliveryState scanned();
    DeliveryState deliveryFailed(String reason);
    DeliveryState delivered();
}

