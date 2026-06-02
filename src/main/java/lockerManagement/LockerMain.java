package lockerManagement;

import lockerManagement.exception.InvalidPinException;
import lockerManagement.exception.InvalidStateTransitionException;
import lockerManagement.exception.NoSlotAvailableException;
import lockerManagement.exception.PackageNotFoundException;
import lockerManagement.model.*;
import lockerManagement.model.Package;
import lockerManagement.notification.ConsoleNotificationService;
import lockerManagement.notification.NotificationService;
import lockerManagement.service.ConsoleReturnService;
import lockerManagement.service.LockerService;
import lockerManagement.service.ReturnService;
import lockerManagement.slotAllocation.BestFitStrategy;

import java.util.ArrayList;
import java.util.List;

public class LockerMain {


        public static void main(String[] args) {

            NotificationService notif = new ConsoleNotificationService();
            ReturnService ret = new ConsoleReturnService();

            List<LockerSlot> slots = new ArrayList<>();
            slots.add(new LockerSlot("S1", Size.SMALL, notif, ret));
            slots.add(new LockerSlot("S2", Size.SMALL, notif, ret));
            slots.add(new LockerSlot("M1", Size.MEDIUM, notif, ret));
            slots.add(new LockerSlot("M2", Size.MEDIUM, notif, ret));
            slots.add(new LockerSlot("L1", Size.LARGE, notif, ret));

            LockerService service = new LockerService(slots, new BestFitStrategy());

            // --- happy path ---
            Package smallPkg = new Package("PKG_001", Size.SMALL, "CUST_1");
            DeliveryReceipt receipt1 = service.assignLocker(smallPkg);
            System.out.println("Assigned slot: " + receipt1.slotId + " PIN: " + receipt1.pin);
            // PKG_001 is SMALL -> goes to S1, not M1 or L1

            service.depositPackage("PKG_001");
            service.pickupPackage("PKG_001", receipt1.pin);   // success

            // --- wrong PIN ---
            Package medPkg = new Package("PKG_002", Size.MEDIUM, "CUST_2");
            DeliveryReceipt receipt2 = service.assignLocker(medPkg);
            service.depositPackage("PKG_002");

            try {
                service.pickupPackage("PKG_002", "000000");   // wrong PIN
            } catch (InvalidPinException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // correct PIN still works after one wrong attempt
            service.pickupPackage("PKG_002", receipt2.pin);

            // --- no slot available ---
            Package largePkg1 = new Package("PKG_003", Size.LARGE, "CUST_3");
            Package largePkg2 = new Package("PKG_004", Size.LARGE, "CUST_4");
            service.assignLocker(largePkg1);   // takes L1

            try {
                service.assignLocker(largePkg2);   // no LARGE slot left
            } catch (NoSlotAvailableException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // --- package not found ---
            try {
                service.depositPackage("PKG_GHOST");
            } catch (PackageNotFoundException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // --- invalid state transition ---
            Package pkg5 = new Package("PKG_005", Size.SMALL, "CUST_5");
            service.assignLocker(pkg5);   // slot is now RESERVED

            try {
                // trying to pickup before deposit — invalid
                LockerSlot reservedSlot = slots.get(0);   // S1 is reserved
                reservedSlot.pickup("123456");
            } catch (InvalidStateTransitionException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // --- maintenance flow ---
            service.markSlotOutOfOrder("M1");
            service.markSlotAvailable("M1");   // maintenance fixed it
        }
    }

