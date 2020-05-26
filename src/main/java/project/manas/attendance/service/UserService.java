package project.manas.attendance.service;


import project.manas.attendance.entity.User;
import project.manas.attendance.model.Role;

import java.util.Optional;

public interface UserService {
    void save(User user);

    Optional<User> findOneByUserName(String userName);

    Integer getNumberOf(Role student);

    void deleteStudent(String id);

    void deleteTeacher(int id);

}
