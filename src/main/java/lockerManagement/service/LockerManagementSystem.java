package lockerManagement.service;

import lockerManagement.exception.InvalidTokenException;
import lockerManagement.exception.NoLockerAvailableException;
import lockerManagement.model.*;
import lockerManagement.notification.NotificationService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LockerManagementSystem {
    private final Map<String, LockerStation> stationRegistry = new ConcurrentHashMap<>();
    private final Map<String, LockerToken> tokenRegistry = new ConcurrentHashMap<>();
    private final NotificationService notificationService;
    private final SecureRandom secureRandom = new SecureRandom();

    public LockerManagementSystem(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void registerStation(LockerStation station) {
        stationRegistry.put(station.getStationId(), station);
    }

    /**
     * Flow Phase 1: Customer orders item. System Reserves a fitting locker.
     */
    public LockerToken reserveLockerForOrder(String stationId, String packageId, LockerSize size) {
        LockerStation station = stationRegistry.get(stationId);
        if (station == null) throw new IllegalArgumentException("Station not found.");

        // Handled cleanly via looping try-lock validation
        Locker locker = station.acquireOptimalLocker(size);
        if (locker == null) {
            throw new NoLockerAvailableException("No lockers available at station " + stationId);
        }

        // The lock is already held by this execution context out of acquireOptimalLocker!
        try {
            locker.reserve(packageId);

            String securePin = String.format("%06d", secureRandom.nextInt(1000000));
            String tokenId = UUID.randomUUID().toString();

            LockerToken token = new LockerToken.Builder()
                    .tokenId(tokenId)
                    .lockerId(locker.getId())
                    .stationId(stationId)
                    .packageId(packageId)
                    .secureCode(securePin)
                    .expiryTime(LocalDateTime.now().plusDays(3)) // 3 Day Default TTL
                    .build();

            tokenRegistry.put(tokenId, token);
            return token;
        } finally {
            locker.getLock().unlock();
        }
    }

    /**
     * Flow Phase 2: Courier arrives at physical location. Checks into reserved slot.
     */
    public void confirmCourierDropoff(String tokenId) {
        LockerToken token = tokenRegistry.get(tokenId);
        if (token == null) throw new InvalidTokenException("Invalid tracking token.");

        LockerStation station = stationRegistry.get(token.getStationId());
        Locker locker = station.getLocker(token.getLockerId());

        locker.getLock().lock();
        try {
            if (locker.getState() != LockerState.RESERVED) {
                throw new IllegalStateException("Locker state is not reserved for incoming dropoff.");
            }

            locker.allocatePackage();
            notificationService.sendPickupCode(token.getPackageId(), token.getSecureCode(), token.getTokenId());
            System.out.println("Courier completed dropoff for package: " + token.getPackageId());
        } finally {
            locker.getLock().unlock();
        }
    }

    /**
     * Flow Phase 3: Customer pulls package out safely via atomic ticket check.
     */
    public void customerRetrievePackage(String tokenId, String customerInputCode) {
        LockerToken token = tokenRegistry.get(tokenId);
        if (token == null || !token.isValid(customerInputCode)) {
            throw new InvalidTokenException("Invalid, expired, or previously redeemed credential.");
        }

        LockerStation station = stationRegistry.get(token.getStationId());
        Locker locker = station.getLocker(token.getLockerId());

        locker.getLock().lock();
        try {
            // Thread-safe atomic isolation layer check
            if (!token.attemptRedemption()) {
                throw new InvalidTokenException("Token was already processed concurrently.");
            }

            locker.clear();
            tokenRegistry.remove(tokenId);
            station.returnToAvailablePool(locker);

            System.out.println("Package successfully dropped to client. Locker cleared.");
        } finally {
            locker.getLock().unlock();
        }
    }

    /**
     * Sweeper Strategy: Acknowledges skipped items explicitly.
     * Skipped items under contention are left for cleanup during the subsequent cron recurrence.
     */
    public void sweepExpiredTimeouts() {
        for (LockerStation station : stationRegistry.values()) {
            for (Locker locker : station.getAllLockers()) {

                // Fast volatile skip check before lock entry
                if (locker.getState() != LockerState.RESERVED && locker.getState() != LockerState.OCCUPIED) {
                    continue;
                }

                if (locker.getLock().tryLock()) {
                    try {
                        // Double check state inside lock boundaries
                        if (locker.getState() == LockerState.RESERVED || locker.getState() == LockerState.OCCUPIED) {

                            // Discover active token by searching values (or maintain separate reverse index)
                            LockerToken token = tokenRegistry.values().stream()
                                    .filter(t -> t.getLockerId().equals(locker.getId()))
                                    .findFirst().orElse(null);

                            if (token != null && token.isExpired()) {
                                String pkg = locker.getCurrentPackageId();
                                locker.clear();
                                tokenRegistry.remove(token.getTokenId());
                                station.returnToAvailablePool(locker);

                                notificationService.sendExpiryAlert(pkg, locker.getId());
                            }
                        }
                    } finally {
                        locker.getLock().unlock();
                    }
                } else {
                    // Explicitly documented assumption: If lock acquisition fails, the asset is currently
                    // undergoing active operation (retrieval/delivery). Skip safely; the next scheduled pass will reclaim it.
                    System.out.println("Locker " + locker.getId() + " under active load. Skipping sweep for this iteration.");
                }
            }
        }
    }
}