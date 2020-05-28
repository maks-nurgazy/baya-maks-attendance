package project.manas.attendance.service.impl;

import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Fingerprint;
import project.manas.attendance.service.FingerCompareService;

import java.util.List;

@Service
public class FingerCompareServiceImpl implements FingerCompareService {


    List<Fingerprint>candidates;

    @Override
    public void setCandidates(List<Fingerprint> candidates) {
        this.candidates = candidates;
    }

    @Override
    public Fingerprint compare(byte[] fingerprintByte) {
        FingerprintTemplate probe = new FingerprintTemplate(fingerprintByte);
        return find(probe);
    }


    private Fingerprint find(FingerprintTemplate probe){
        FingerprintMatcher matcher = new FingerprintMatcher()
                .index(probe);
        Fingerprint match = null;
        double high = 0;
        for (Fingerprint candidate : candidates) {
            FingerprintTemplate template = new FingerprintTemplate(candidate.getSample());
            double score = matcher.match(template);
            if (score > high) {
                high = score;
                match = candidate;
            }
        }
        double threshold = 40;
        return high >= threshold ? match : null;
    }

}
