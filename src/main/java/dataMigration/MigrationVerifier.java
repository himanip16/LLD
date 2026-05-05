package dataMigration;

import dataMigration.alert.AlertService;
import dataMigration.enums.EventType;
import dataMigration.enums.MigrationStatus;
import dataMigration.models.MigrationEvent;
import dataMigration.repo.StateRepository;

import java.util.concurrent.*;

public class MigrationVerifier {
    private final StateRepository repository;
    private final AlertService alertService;
    private final ScheduledExecutorService scheduler;
    private final long kSeconds;

    public MigrationVerifier(StateRepository repository, AlertService alertService, long kSeconds) {
        this.repository = repository;
        this.alertService = alertService;
        this.kSeconds = kSeconds;
        this.scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Entry point for all incoming events from the message bus (Kafka/RabbitMQ)
     */
    public void processEvent(MigrationEvent event) {
        if (event.type() == EventType.SOURCE_EMITTED) {
            handleSourceEvent(event.recordId());
        } else if (event.type() == EventType.DESTINATION_RECEIVED) {
            handleDestinationEvent(event.recordId());
        }
    }

    private void handleSourceEvent(String recordId) {
        repository.markPending(recordId);

        // Schedule the verification task to run exactly k seconds from now
        scheduler.schedule(() -> verify(recordId), kSeconds, TimeUnit.SECONDS);
    }

    private void handleDestinationEvent(String recordId) {
        repository.markCompleted(recordId);
    }

    private void verify(String recordId) {
        MigrationStatus status = repository.getStatus(recordId);

        // If it's still pending, the k-second window has closed without a success event
        if (status == MigrationStatus.PENDING) {
            alertService.sendAlert(recordId, kSeconds);
        }

        // Always cleanup to prevent memory leaks
        repository.cleanup(recordId);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}