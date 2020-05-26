package project.manas.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.manas.attendance.entity.User;
import project.manas.attendance.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String teacherDetails(Model model, Principal principal) {
        Optional<User> user = userService.findOneByUserName(principal.getName());
        model.addAttribute("teacherDetails", user.get());
        model.addAttribute("subjectList", "Math");
        return "teacher-details";
    }

}
