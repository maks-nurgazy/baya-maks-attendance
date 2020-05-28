package project.manas.attendance.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.form.AttendanceForm;
import project.manas.attendance.service.AttendanceService;

import java.security.Principal;
import java.util.List;

@Controller
public class AttendanceController {

    AttendanceService attendanceService;


    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance")
    public String attendanceList(Model model, Principal principal) {
        List<Subject> subjects = attendanceService.getTeacherSubjects(principal);
        model.addAttribute("subjects", subjects);
        return "attendance/attendanceList";
    }

    @PostMapping("/saveAttendance")
    @ResponseBody
    public String saveAttendance(@RequestParam AttendanceForm name){
        return "hello";
    }

}
