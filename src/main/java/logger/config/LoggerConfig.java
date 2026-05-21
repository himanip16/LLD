package logger.config;

import logger.models.LogLevel;
import logger.sinks.LogSink;
import java.util.HashMap;
import java.util.Map;

public class LoggerConfig {
    private final String timeFormat;
    private final LogLevel globalThresholdLevel;
    private final Map<LogLevel, LogSink> levelSinkRegistry = new HashMap<>();

    public LoggerConfig(String timeFormat, LogLevel globalThresholdLevel) {
        this.timeFormat = timeFormat;
        this.globalThresholdLevel = globalThresholdLevel;
    }

    public void assignSinkToLevel(LogLevel level, LogSink sink) {
        levelSinkRegistry.put(level, sink);
    }

    public String getTimeFormat() { return timeFormat; }
    public LogLevel getGlobalThresholdLevel() { return globalThresholdLevel; }
    public LogSink getSinkForLevel(LogLevel level) { return levelSinkRegistry.get(level); }
}