package bookMyshow.model;

import bookMyshow.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Payment {
    private int id;
    private PaymentStatus paymentStatus;
    private double amount;

}
