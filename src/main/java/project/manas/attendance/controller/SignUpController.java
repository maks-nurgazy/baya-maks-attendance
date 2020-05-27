package project.manas.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.manas.attendance.form.UserForm;
import project.manas.attendance.service.SignUpService;

@Controller
public class SignUpController {

    private SignUpService service;

    @Autowired
    public void setService(SignUpService service) {
        this.service = service;
    }

    @GetMapping("/signUp")
    public String getSignUpPage() {
        return "entry/signUp";
    }

    @PostMapping("/signUp")
    public String signUp(UserForm userForm) {
        service.signUp(userForm);
        return "redirect:/signIn";
    }

}
