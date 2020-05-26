package project.manas.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.service.StudentService;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {

    StudentService studentService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public String index3(Model model, Principal principal) {
        Student studentDetails = studentService.getStudentByUsername(principal.getName());
        model.addAttribute("studentDetails", studentDetails);
        return "index3";
    }

    @GetMapping("/student-details")
    public String studentDetails(@RequestParam("studentId") String id, Model model) {
        Student studentDetails = studentService.getStudentById(id);
        StringBuilder subjectList = new StringBuilder();
        for (Subject subject : studentDetails.getSubjects()) {
            subjectList.append(subject.getName()).append(", ");
        }
        model.addAttribute("studentDetails", studentDetails);
        model.addAttribute("subjectList", subjectList.toString());
        return "student-details";
    }


}
