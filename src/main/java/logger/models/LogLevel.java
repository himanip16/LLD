package logger.models;

public enum LogLevel {
    // Priority order: FATAL (highest severity) down to DEBUG (lowest severity)
    FATAL(5),
    ERROR(4),
    WARN(3),
    INFO(2),
    DEBUG(1);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}