package logger.sinks;

public class ConsoleSink implements LogSink {
    @Override
    public void write(String enrichedMessage) {
        System.out.print("[Console] " + enrichedMessage);
    }

    @Override
    public String getSinkType() { return "CONSOLE"; }
}

