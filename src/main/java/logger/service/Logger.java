package logger.service;

import logger.config.LoggerConfig;
import logger.models.LogLevel;
import logger.sinks.LogSink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final LoggerConfig config;
    private final DateTimeFormatter dateFormatter;

    public Logger(LoggerConfig config) {
        this.config = config;
        this.dateFormatter = DateTimeFormatter.ofPattern(config.getTimeFormat());
    }

    /**
     * Core public API for client application logging intake pipeline.
     */
    public void log(String content, LogLevel level, String namespace) {
        // 1. Priority-Based Filtering Guard Rule
        // Example: If configured level is INFO (2), allow WARN (3), INFO (2), DEBUG (1)
        // strictly according to your prompt example constraint rule requirement logic.
        if (!isLevelAllowedByConfig(level)) {
            return;
        }

        // 2. Identify Target Destination Sink
        LogSink assignedSink = config.getSinkForLevel(level);
        if (assignedSink == null) {
            System.err.println("⚠️ No matching sink registered to process log severity: " + level);
            return;
        }

        // 3. Log Enrichment Phase
        String enrichedPayload = enrichMessage(content, level, namespace);

        // 4. Thread-safe routing dispatch execution step
        assignedSink.write(enrichedPayload);
    }

    private boolean isLevelAllowedByConfig(LogLevel incomingLevel) {
        LogLevel configuredThreshold = config.getGlobalThresholdLevel();

        // Special structural handling matching your exact prompt parameter criteria:
        // "If configured as INFO, only WARN, INFO, and DEBUG messages will be logged."
        // This indicates an "inclusive vicinity/radius around priority" or a specific evaluation rule.
        // Usually, in standard architecture, "above INFO" means higher priority numbers.
        // To match the prompt scenario precisely:
        if (configuredThreshold == LogLevel.INFO) {
            return incomingLevel == LogLevel.WARN || incomingLevel == LogLevel.INFO || incomingLevel == LogLevel.DEBUG;
        }

        // Standard priority boundary fallback loop
        return incomingLevel.getPriority() >= configuredThreshold.getPriority();
    }

    private String enrichMessage(String rawContent, LogLevel level, String namespace) {
        String timestamp = LocalDateTime.now().format(dateFormatter);
        // Formats line cleanly as an atomic structural snapshot trace string block
        return String.format("[%s] [%s] [%s] - %s%n", timestamp, level, namespace, rawContent);
    }
}