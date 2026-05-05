package dataMigration.models;

import dataMigration.enums.EventType;

public record MigrationEvent(String recordId, long timestamp, EventType type) {}