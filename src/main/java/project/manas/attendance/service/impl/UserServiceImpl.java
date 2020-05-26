package project.manas.attendance.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Teacher;
import project.manas.attendance.entity.User;
import project.manas.attendance.model.Role;
import project.manas.attendance.repository.UserRepository;
import project.manas.attendance.service.StudentService;
import project.manas.attendance.service.TeacherService;
import project.manas.attendance.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private TeacherService teacherService;
    private StudentService studentService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUserName(String userName) {
        return userRepository.findOneByUserName(userName);
    }

    @Override
    public Integer getNumberOf(Role role) {
        return userRepository.countByRole(role);
    }

    @Override
    @Transactional
    public void deleteStudent(String id) {
        Student studentById = studentService.getStudentById(id);
        userRepository.deleteUsersByUserName(studentById.getUserName());
    }

    @Override
    @Transactional
    public void deleteTeacher(int id) {
        Teacher teacher = teacherService.getTeacherById(id);
        userRepository.deleteUsersByUserName(teacher.getUserName());
    }


}
