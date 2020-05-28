package project.manas.attendance.service;

import project.manas.attendance.entity.Fingerprint;

import java.util.List;

public interface FingerCompareService {
    Fingerprint compare(byte[] fingerprintByte);
    void setCandidates(List<Fingerprint> candidates);
}

