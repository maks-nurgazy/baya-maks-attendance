package project.manas.attendance.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.User;
import project.manas.attendance.form.UserForm;
import project.manas.attendance.service.SignUpService;
import project.manas.attendance.service.UserService;

@Service
public class SignUpServiceImpl implements SignUpService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUp(UserForm userForm) {
        String hashPassword = passwordEncoder.encode(userForm.getPassword());

        User user = User.builder()
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .hashPassword(hashPassword)
                .userName(userForm.getUserName())
                .role(userForm.getRole())
                .build();

        userService.save(user);
    }
}