package atm;

import atm.model.Card;
import atm.service.ATM;
import atm.service.BankService;

public class Main {
    public static void main(String[] args) {
        BankService bankServer = new BankService();
        ATM atmDevice = new ATM(bankServer);

        System.out.println("=== ATM Transaction Flow Simulation ===");
        Card customerCard = new Card("CARD_IN_456", "SAFE_HASH_REF");

        // Step 1: User interacts with terminal
        atmDevice.insertCard(customerCard);

        // Step 2: Input Authorization Token
        atmDevice.enterPin("4321");

        // Step 3: Run operations safely inside a unified environment state
        atmDevice.checkBalance();
        atmDevice.executeWithdrawal(3700); // Expects: 7x500 + 1x200
        atmDevice.executeWithdrawal(13900); // Expects: 7x500 + 1x200
        atmDevice.checkBalance();

        // Step 4: Clear terminal state boundaries
        atmDevice.exitSession();
    }
}
