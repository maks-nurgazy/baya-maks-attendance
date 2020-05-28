package project.manas.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.manas.attendance.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {


}
