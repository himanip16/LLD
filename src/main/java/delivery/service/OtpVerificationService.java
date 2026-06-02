package delivery.service;

public interface OtpVerificationService {
    boolean validateOtp(String packageId, String inputOtp);
}