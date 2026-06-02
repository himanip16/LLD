package parkingLot.gate;

import parkingLot.model.Ticket;
import parkingLot.service.ParkingLot;
import parkingLot.spot.ParkingSpot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class ExitGate {
    private final String gateId;
    private final ParkingLot parkingLot;
    private final Map<String, Ticket> centralizedLedger; // Simulates remote transactional DB database layer

    public ExitGate(String gateId, ParkingLot lot, Map<String, Ticket> ledger) {
        this.gateId = gateId;
        this.parkingLot = lot;
        this.centralizedLedger = ledger;
    }

    public void processVehicleExit(String ticketId) {
        Ticket ticket = centralizedLedger.remove(ticketId);
        if (ticket == null) {
            System.out.println("[EXIT GATE " + gateId + "]: Error - Invalid, counterfeit or expired ticket token.");
            return;
        }

        ParkingSpot spot = ticket.getSpot();

        // Calculate costs completely outside state manager boundaries
        long durationHours = Duration.between(ticket.getStartTime(), LocalDateTime.now().plusHours(4)).toHours();
        double fee = Math.max(1, durationHours) * 20.0;

        // Atomic hardware and collection tracking changes execution
        spot.removeVehicle();
        parkingLot.getFloors().get(spot.getFloorNumber()).getFreeSpots(spot.getSpotType()).add(spot);
        parkingLot.incrementCapacity(spot.getSpotType());

        System.out.println("[EXIT GATE " + gateId + "]: Processed " + ticketId + ". Paid Fee: " + fee + ". Barrier Open.");
    }
}
