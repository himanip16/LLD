package lockerManagement.service;

import java.security.SecureRandom;

public class PinGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        int pin = 100000 + random.nextInt(900000);   // always 6 digits
        return String.valueOf(pin);
    }
}
