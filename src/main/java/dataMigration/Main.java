package dataMigration;

import dataMigration.alert.AlertService;
import dataMigration.alert.MigrationAlertService;
import dataMigration.enums.EventType;
import dataMigration.models.MigrationEvent;
import dataMigration.repo.StateRepository;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        StateRepository repo = new StateRepository();
        AlertService alerts = new MigrationAlertService();

        // Threshold k = 2 seconds
        MigrationVerifier verifier = new MigrationVerifier(repo, alerts, 2);

        // Scenario 1: Successful Migration within 2s
        System.out.println("Starting Scenario 1 (Fast migration)...");
        verifier.processEvent(new MigrationEvent("REC-001", System.currentTimeMillis(), EventType.SOURCE_EMITTED));
        Thread.sleep(1000); // 1 second passes
        verifier.processEvent(new MigrationEvent("REC-001", System.currentTimeMillis(), EventType.DESTINATION_RECEIVED));

        // Scenario 2: Slow Migration (Triggers Alert)
        System.out.println("Starting Scenario 2 (Slow migration)...");
//        verifier.processEvent(new MigrationEvent("REC-999", System.currentTimeMillis(), EventType.SOURCE_EMITTED));

        // Wait 3 seconds to ensure the 2s timer expires
        Thread.sleep(3000);

        verifier.shutdown();
    }
}