package atm.model;

import java.util.Date;

public class Card {
    private final String cardNumber;
    private final String hashedPin;

    public Card(String cardNumber, String hashedPin) {
        this.cardNumber = cardNumber;
        this.hashedPin = hashedPin;
    }
    public String getCardNumber() { return cardNumber; }
    public String getHashedPin() { return hashedPin; }
}