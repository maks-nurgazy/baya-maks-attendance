package project.manas.attendance.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.User;
import project.manas.attendance.model.Gender;
import project.manas.attendance.model.Role;
import project.manas.attendance.repository.StudentRepository;
import project.manas.attendance.service.StudentService;
import project.manas.attendance.service.SubjectService;
import project.manas.attendance.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private SubjectService subjectService;
    private UserService userService;

    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
    public Student getStudentByUsername(String userName) {
        return studentRepository.findStudentByUserName(userName);
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.getAllStudents();
    }

    @Override
    public Student getStudentById(String id) {
        Optional<Student> byId = studentRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public void saveStudent(Student student) {
        User user = User.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .userName(student.getUserName())
                .hashPassword("$2a$10$n7D9JGargKbNVLIWvhYR2OsQwDHeycuLT0aYCisekQK.Y2k0SB.Qe")
                .role(Role.STUDENT)
                .build();
        student.setUserName(student.getId());
        userService.save(user);
        studentRepository.save(student);
    }

    @Override
    public void deleteStudent(String id) {
        userService.deleteStudent(id);
        studentRepository.deleteById(id);
    }

    @Override
    public Integer countByGender(Gender gender) {
        return studentRepository.countByGender(gender);
    }

    @Override
    public void saveUpdatedStudent(Student student) {
        studentRepository.save(student);
    }

}
