package project.manas.attendance.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Subject findSubjectByName(String name);

    Subject findSubjectByTeacherAndName(Teacher teacher, String subjectName);

    List<Subject> findSubjectByTeacherUserName(String userName);

    List<Subject> findSubjectsByTeacherId(int id);

    List<Subject> findSubjectByStudentsIs(Student student);

}
