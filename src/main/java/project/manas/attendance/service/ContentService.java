package project.manas.attendance.service;


import project.manas.attendance.entity.Student;

import java.util.List;

public interface ContentService {
    List<Student>getStudentList(String subjectName, String teacherUsername);
}
