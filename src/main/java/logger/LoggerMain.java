package logger;

import logger.config.LoggerConfig;
import logger.models.LogLevel;
import logger.sinks.ConsoleSink;
import logger.sinks.FileSink;
import logger.sinks.LogSink;
import logger.service.Logger;

public class LoggerMain {
    public static void main(String[] args) {
        // 1. Initialize Sinks destinations
        LogSink console = new ConsoleSink();
        LogSink fileAppender = new FileSink("/var/logs/application.log");

        // 2. Construct the configuration state entity context
        // Time format spec matching requirement target example profile pattern
        LoggerConfig appConfig = new LoggerConfig("yyyy-MM-dd HH:mm:ss", LogLevel.INFO);

        // Map levels to distinct shared or individual sinks according to constraints
        appConfig.assignSinkToLevel(LogLevel.FATAL, fileAppender);
        appConfig.assignSinkToLevel(LogLevel.ERROR, fileAppender);
        appConfig.assignSinkToLevel(LogLevel.WARN, fileAppender);
        appConfig.assignSinkToLevel(LogLevel.INFO, console);
        appConfig.assignSinkToLevel(LogLevel.DEBUG, console);

        // 3. Spin up our active log lifecycle core coordinator engine instance
        Logger logSystem = new Logger(appConfig);

        System.out.println("=== Starting Logger Intake Routing Processing Simulation ===");

        // Scenario Execution A: Log an INFO level trace message from Authentication Namespace
        // Result Expectation: Enriched, mapped to ConsoleSink, allowed via priority matching.
        logSystem.log("User 'Himani' successfully logged in via OAuth2 channel.", LogLevel.INFO, "spotify.auth");

        // Scenario Execution B: Log an ERROR level block trace from Data Service Layer Namespace
        // Result Expectation: Enriched, routed directly to the system File location destination.
        logSystem.log("Database connection pool timeouts detected on master cluster instance node.", LogLevel.ERROR, "spotify.database");

        // Scenario Execution C: Log a FATAL level block trace message
        // Result Expectation: Configured threshold filter drops this because threshold is INFO (allows WARN, INFO, DEBUG)
        logSystem.log("Core system out of memory crash imminent.", LogLevel.FATAL, "spotify.kernel");
    }
}