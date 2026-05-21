package logger.sinks;

public class FileSink implements LogSink {
    private final String fileLocation;

    public FileSink(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    @Override
    public void write(String enrichedMessage) {
        // In a production system, this would write via a BufferedWriter.
        // For simulation, we print the destination target channel.
        System.out.print("[File -> " + fileLocation + "] " + enrichedMessage);
    }

    @Override
    public String getSinkType() {
        return "FILE";
    }
}
