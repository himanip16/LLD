package parkingLot;

import parkingLot.gate.EntryGate;
import parkingLot.gate.ExitGate;
import parkingLot.model.ParkingFloor;
import parkingLot.model.Ticket;
import parkingLot.observer.ElectronicDisplayBoard;
import parkingLot.service.ParkingLot;
import parkingLot.spot.ParkingSpot;
import parkingLot.spotAllocation.ProgressiveFallbackStrategy;
import parkingLot.spotAllocation.SpotAssignmentStrategy;
import parkingLot.vehicle.Car;
import parkingLot.vehicle.Vehicle;
import parkingLot.vehicle.VehicleType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        // Step 1: Initialize State Manager
        ParkingLot lot = new ParkingLot();

        // Step 2: Wire up our Observer layer
        ElectronicDisplayBoard mainBoard = new ElectronicDisplayBoard("MAIN-ENTRANCE-BOARD");
        lot.subscribeObserver(mainBoard);

        // Step 3: Populate Structural Floor map configurations
        ParkingFloor f0 = new ParkingFloor(0);
        f0.addSpot(new ParkingSpot("CAR-F0-01", 0,  VehicleType.CAR));
        lot.addFloor(f0);

        // Step 4: Provision Distributed Gates
        SpotAssignmentStrategy strategy = new ProgressiveFallbackStrategy();
        Map<String, Ticket> databaseLedger = new ConcurrentHashMap<>();

        EntryGate gateAlpha = new EntryGate("ALPHA", lot, strategy);
        ExitGate gateOmega = new ExitGate("OMEGA", lot, databaseLedger);

        System.out.println("\n--- Execution Flow Event Simulation ---");
        Vehicle familyCar = new Car("KA-51-MM-9999");

        // Vehicle drives through entry point Alpha kiosk
        Ticket activeTicket = gateAlpha.processVehicleEntry(familyCar);

        if (activeTicket != null) {
            databaseLedger.put(activeTicket.getTicketId(), activeTicket); // Save to shared ledger storage

            // Vehicle drives to exit gate endpoint kiosk after some time
            gateOmega.processVehicleExit(activeTicket.getTicketId());
        }
    }
}