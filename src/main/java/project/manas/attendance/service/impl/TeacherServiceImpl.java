package project.manas.attendance.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;
import project.manas.attendance.entity.User;
import project.manas.attendance.model.Role;
import project.manas.attendance.repository.TeacherRepository;
import project.manas.attendance.service.SubjectService;
import project.manas.attendance.service.TeacherService;
import project.manas.attendance.service.UserService;

import java.util.*;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;
    private SubjectService subjectService;
    private UserService userService;

    @Autowired
    public void setTeacherRepository(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Teacher getTeacherByUsername(String userName) {
        return teacherRepository.findTeacherByUserName(userName);
    }

    @Override
    public List<Teacher> getAllTeacher() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(int id) {
        Optional<Teacher> byId = teacherRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        User user = User.builder()
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .userName(teacher.getUserName())
                .hashPassword("$2a$10$n7D9JGargKbNVLIWvhYR2OsQwDHeycuLT0aYCisekQK.Y2k0SB.Qe")
                .role(Role.TEACHER)
                .build();
        userService.save(user);
        teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(int id) {
        List<Subject> subjects = subjectService.findSubjectsByTeacherId(id);
        for (Subject subject:subjects){
            subject.setTeacher(null);
        }
        userService.deleteTeacher(id);
        teacherRepository.deleteById(id);
    }

    @Override
    public List<Student> getTeacherStudents(String userName) {
        Teacher teacher = teacherRepository.findTeacherByUserName(userName);
        List<Student>students = new ArrayList<>();
        HashMap<String, Student>hashMap = new HashMap<>();
        for (Subject subject: teacher.getSubjects()){
            List<Student> s = subject.getStudents();
            for (Student student:s){
                hashMap.put(student.getId(),student);
            }
        }
        for (Map.Entry<String, Student> entry : hashMap.entrySet()) {
            students.add(entry.getValue());
        }
        return students;
    }

    @Override
    public List<Subject> getTeacherSubjects(String userName) {
        Teacher teacher = teacherRepository.findTeacherByUserName(userName);
        return teacher.getSubjects();
    }

    @Override
    public List<Student> getStudentList(String subjectName, String userName) {
        Teacher teacher = teacherRepository.findTeacherByUserName(userName);
        Subject subject = subjectService.findSubjectByTeacherAndName(teacher, subjectName);
        return subject.getStudents();
    }

    @Override
    public List<Teacher> getAllTeacherOrderByFirstName() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Student> getStudentList(Integer subjectId) {
        return subjectService.findSubjectById(subjectId).getStudents();
    }

}
