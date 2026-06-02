//package atm.cash;
//
//import atm.model.DenominationCassette;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DynamicCashDispenser implements ICashDispenser {
//    private final List<DenominationCassette> cassettes;
//
//    public DynamicCashDispenser(List<DenominationCassette> cassettes) {
//        cassettes.sort((c1, c2) -> Integer.compare(c2.getNoteValue(), c1.getNoteValue()));
//        this.cassettes = cassettes;
//    }
//
//    @Override
//    public synchronized void dispense(int amount) {
//        int remaining = amount;
//        Map<DenominationCassette, Integer> plan = new HashMap<>();
//
//        // Greedy strategy dry run
//        for (DenominationCassette cassette : cassettes) {
//            int notesNeeded = remaining / cassette.getNoteValue();
//            if (notesNeeded > 0) {
//                int toTake = Math.min(notesNeeded, cassette.getAvailableNotes());
//                if (toTake > 0) {
//                    plan.put(cassette, toTake);
//                    remaining -= toTake * cassette.getNoteValue();
//                }
//            }
//        }
//
//        if (remaining > 0) throw new ATMException("Cannot dispense exact change requested.");
//
//        // Safe to mutate state now
//        for (Map.Entry<DenominationCassette, Integer> entry : plan.entrySet()) {
//            entry.getKey().deductNotes(entry.getValue());
//            System.out.println("[HARDWARE] Dispensed " + entry.getValue() + " x ₹" + entry.getKey().getNoteValue());
//        }
//    }
//}
