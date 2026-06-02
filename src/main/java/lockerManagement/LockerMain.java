package lockerManagement;

import lockerManagement.model.*;
import lockerManagement.notification.NotificationService;
import lockerManagement.notification.SmsNotificationService;
import lockerManagement.service.LockerManagementSystem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class LockerMain {

    public static void main(String[] args) {
        // 1. Initialize Subsystems & Metadata
        NotificationService notificationService = new SmsNotificationService();
        LockerManagementSystem lms = new LockerManagementSystem(notificationService);

        Location stationLocation = new Location(12.9716, 77.5946, "td3377"); // Bangalore center coords

        // 2. Provision a Locker Bank (Station) with assorted sizes
        List<Locker> initialLockers = Arrays.asList(
                new Locker("L-SMALL-01", LockerSize.S, "STN-BLR-01"),
                new Locker("L-MED-02", LockerSize.M, "STN-BLR-01"),
                new Locker("L-LARGE-03", LockerSize.L, "STN-BLR-01")
        );

        LockerStation bangaloreStation = new LockerStation(
                "STN-BLR-01",
                "MG Road Metro Station Bank",
                stationLocation,
                initialLockers
        );

        lms.registerStation(bangaloreStation);
        System.out.println("=== Locker Station Registered Successfully ===\n");

        // ====================================================================
        // SCENARIO A: Standard Happy Path (Reserve -> Dropoff -> Retrieve)
        // ====================================================================
        System.out.println("--- Scenario A: Happy Path Flow ---");

        // Step 1: Customer orders a Medium item. System reserves an optimal locker.
        String packageA = "PKG-AMZN-998822";
        LockerToken tokenA = lms.reserveLockerForOrder("STN-BLR-01", packageA, LockerSize.M);
        System.out.println("Order Placed. Reserved Locker ID: " + tokenA.getLockerId());

        // Step 2: Courier arrives at the station and inputs the tracking token to drop it off
        lms.confirmCourierDropoff(tokenA.getTokenId());

        // Step 3: Customer arrives, enters their 6-digit PIN code, and pulls the item out
        String customerInputCode = tokenA.getSecureCode();
        lms.customerRetrievePackage(tokenA.getTokenId(), customerInputCode);
        System.out.println();

        // ====================================================================
        // SCENARIO B: Timeout Eviction Flow (Expired Reservation/Occupancy)
        // ====================================================================
        System.out.println("--- Scenario B: Expiration Sweeper Flow ---");

        String packageB = "PKG-AMZN-112233";
        // Reserve a Small locker
        LockerToken tokenB = lms.reserveLockerForOrder("STN-BLR-01", packageB, LockerSize.S);
        System.out.println("Order Placed. Reserved Locker ID: " + tokenB.getLockerId());

        // Courier drops it off
        lms.confirmCourierDropoff(tokenB.getTokenId());

        System.out.println("\n[System Simulation]: Simulating time jump past 3 days...");
        // Fast-forwarding time directly inside our current registry token for testing
        // Hacky override just for simulation without adding complex clock providers
        try {
            java.lang.reflect.Field expiryField = LockerToken.class.getDeclaredField("expiryTime");
            expiryField.setAccessible(true);
            expiryField.set(tokenB, LocalDateTime.now().minusDays(1)); // set past expiry
        } catch (Exception e) {
            System.out.println("Simulation reflection failed: " + e.getMessage());
        }

        // Trigger the background sweeper job
        System.out.println("Executing Cron Sweeper Engine Job...");
        lms.sweepExpiredTimeouts();

        // Verify locker availability restoration
        System.out.println("\n--- Post-Sweep Invariant Check ---");
        Locker lockerAfterSweep = bangaloreStation.getLocker(tokenB.getLockerId());
        System.out.println("Locker " + lockerAfterSweep.getId() + " Current State: " + lockerAfterSweep.getState());
    }
}

