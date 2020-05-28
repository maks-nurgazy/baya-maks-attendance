package project.manas.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.manas.attendance.entity.Fingerprint;

import java.util.List;
import java.util.Optional;

public interface FingerprintRepository extends JpaRepository<Fingerprint,Integer> {
    Optional<List<Fingerprint>> findFingerprintByStudentId(String studentId);
    Integer countByStudentId(String id);
}
