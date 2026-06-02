package parkingLot.gate;

import parkingLot.model.Ticket;
import parkingLot.service.ParkingLot;
import parkingLot.spot.ParkingSpot;
import parkingLot.spotAllocation.SpotAssignmentStrategy;
import parkingLot.vehicle.Vehicle;

public class EntryGate {
    private final String gateId;
    private final ParkingLot parkingLot;
    private final SpotAssignmentStrategy assignmentStrategy;

    public EntryGate(String gateId, ParkingLot parkingLot, SpotAssignmentStrategy strategy) {
        this.gateId = gateId;
        this.parkingLot = parkingLot;
        this.assignmentStrategy = strategy;
    }

    public Ticket processVehicleEntry(Vehicle vehicle) {
        System.out.println("[ENTRY GATE " + gateId + "]: Processing vehicle " + vehicle.getLicensePlate());

        ParkingSpot reservedSpot = assignmentStrategy.findAndReserveSpot(parkingLot.getFloors(), vehicle);
        if (reservedSpot != null) {
            Ticket ticket = new Ticket(vehicle, reservedSpot);

            // Mutate purely isolated statistical metadata asynchronously
            parkingLot.decrementCapacity(reservedSpot.getSpotType());
            System.out.println("[ENTRY GATE " + gateId + "]: Created Ticket " + ticket.getTicketId() + " -> Park at spot " + reservedSpot.getSpotId());
            return ticket;
        }

        System.out.println("[ENTRY GATE " + gateId + "]: Entry Denied! No compatible capacity found.");
        return null;
    }
}