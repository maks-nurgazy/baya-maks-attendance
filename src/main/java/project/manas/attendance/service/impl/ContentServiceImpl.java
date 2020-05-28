package project.manas.attendance.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;
import project.manas.attendance.service.ContentService;
import project.manas.attendance.service.SubjectService;
import project.manas.attendance.service.TeacherService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ContentServiceImpl implements ContentService {

    private TeacherService teacherService;
    private SubjectService subjectService;


    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public List<Student> getStudentList(String subjectName, String teacherUsername) {
        Teacher teacher = teacherService.getTeacherByUsername(teacherUsername);
        Subject subject = subjectService.findSubjectByTeacherAndName(teacher, subjectName);
        Set<String> d = new HashSet<>();
        List<Student> students = new ArrayList<>();
        for (Student student:subject.getStudents())
        {
            if (!d.contains(student.getId())){
                d.add(student.getId());
                students.add(student);
            }
        }
        return students;
    }
}
