package dataMigration.repo;

import dataMigration.enums.MigrationStatus;

import java.util.concurrent.ConcurrentHashMap;

public class StateRepository {
    private final ConcurrentHashMap<String, MigrationStatus> storage = new ConcurrentHashMap<>();

    public void markPending(String recordId) {
        storage.putIfAbsent(recordId, MigrationStatus.PENDING);
    }

    public void markCompleted(String recordId) {
        storage.put(recordId, MigrationStatus.COMPLETED);
    }

    public MigrationStatus getStatus(String recordId) {
        return storage.get(recordId);
    }

    public void cleanup(String recordId) {
        storage.remove(recordId);
    }
}
