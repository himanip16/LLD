package dataMigration.alert;

public interface AlertService {
    void sendAlert(String recordId, long lagSeconds);
}

