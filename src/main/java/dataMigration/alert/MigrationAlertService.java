package dataMigration.alert;

public class MigrationAlertService implements AlertService {

    @Override
    public void sendAlert(String recordId, long lagSeconds) {
        System.err.printf("[ALERT] Record %s failed to migrate within %d seconds!%n",
                recordId, lagSeconds);
    }
}
