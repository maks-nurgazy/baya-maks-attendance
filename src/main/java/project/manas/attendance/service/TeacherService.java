package project.manas.attendance.service;


import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher getTeacherByUsername(String userName);
    List<Teacher> getAllTeacher();
    Teacher getTeacherById(int id);
    void saveTeacher(Teacher teacher);
    void deleteTeacher(int id);
    List<Student> getTeacherStudents(String userName);
    List<Subject> getTeacherSubjects(String userName);
    List<Student> getStudentList(String subjectName, String userName);
    List<Teacher> getAllTeacherOrderByFirstName();
    List<Student> getStudentList(Integer subjectId);
}
