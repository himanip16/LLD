package logger.sinks;

public interface LogSink {
    void write(String enrichedMessage);
    String getSinkType();
}