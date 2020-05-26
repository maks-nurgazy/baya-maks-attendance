package project.manas.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.manas.attendance.entity.Student;
import project.manas.attendance.model.Gender;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findStudentByUserName(String userName);
    Integer countByGender(Gender gender);
    @Query(value = "select * from Student",nativeQuery = true)
    List<Student> getAllStudents();
}
