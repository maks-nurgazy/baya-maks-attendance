package project.manas.attendance.service;

public interface FingerprintService {

    void setStudentId(String studentId);
    String addFingerprintToBuffer(byte[] fingerprintByte);
    void clearFingerprintBuffer();
    void handleMessage(String message);
    String handleImage(byte[] fingerprintByte);
}
