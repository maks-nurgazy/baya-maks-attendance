package project.manas.attendance.service;


import project.manas.attendance.entity.Student;
import project.manas.attendance.model.Gender;

import java.util.List;

public interface StudentService {
    Student getStudentByUsername(String userName);

    List<Student> getAllStudent();

    Student getStudentById(String id);

    void saveStudent(Student student);

    void deleteStudent(String id);

    Integer countByGender(Gender gender);

    void saveUpdatedStudent(Student student);
}
