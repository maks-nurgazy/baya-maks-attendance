package project.manas.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.manas.attendance.entity.User;
import project.manas.attendance.model.Role;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findOneByUserName(String userName);
    User getOneByUserName(String userName);
    Integer countByRole(Role role);
    void deleteUsersByUserName(String userName);
}
