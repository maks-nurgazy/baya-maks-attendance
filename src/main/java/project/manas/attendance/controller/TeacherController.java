package project.manas.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.manas.attendance.entity.Student;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;
import project.manas.attendance.form.Statistic;
import project.manas.attendance.model.MyObject;
import project.manas.attendance.model.Role;
import project.manas.attendance.service.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    TeacherService teacherService;
    SubjectService subjectService;
    StatisticService statisticService;
    StudentService studentService;
    FingerprintService fingerprintService;
    AttendanceService attendanceService;


    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setFingerprintService(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("")
    public String index5(Model model, Principal principal) {
        List<Student> teacherStudents = teacherService.getTeacherStudents(principal.getName());
        Statistic statistic = statisticService.getStudentStatic(teacherStudents);
        model.addAttribute("studentList", teacherStudents);
        model.addAttribute("statistic", statistic);
        return "index5";
    }


    @GetMapping("/teacher-details")
    public String teacherDetails(Model model, Principal principal) {
        Teacher teacherDetails = teacherService.getTeacherByUsername(principal.getName());
//        StringBuilder subjectList = new StringBuilder();
//        for (Subject subject : teacherDetails.getSubjects()) {
//            subjectList.append(subject.getName()).append(", ");
//        }
//        model.addAttribute("subjectList", subjectList.toString());
        model.addAttribute("teacherDetails", teacherDetails);
        return "teacher-details";
    }

    @GetMapping("/all-subject")
    public String allSubject(Model model, Principal principal) {
        List<Subject> allSubject = subjectService.getAllSubject(Role.TEACHER, principal);
        model.addAttribute("subjectList", allSubject);
        return "all-subject";
    }

    @GetMapping("/all-student")
    public String allStudent(Model model, Principal principal) {
        List<Student> allStudent = teacherService.getTeacherStudents(principal.getName());
        model.addAttribute("studentList", allStudent);
        return "all-student";
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

    @GetMapping("/attendance")
    public String attendanceForm(Model model, Principal principal) {
        List<Subject> subjects = teacherService.getTeacherSubjects(principal.getName());
        model.addAttribute("subjectList", subjects);
        return "attendance-form";
    }

    @GetMapping("/attendance-sheet")
    public String attendanceSheet(Model model) {
        return "student-attendance";
    }

    @GetMapping("/students")
    public String studentList(@PathParam("subjectId") Integer subjectId, Model model) {
        List<Student> students = teacherService.getStudentList(subjectId);
        model.addAttribute("students", students);
        attendanceService.setSubjectId(subjectId);
        return "fragment/attendance-form :: student-list";
    }

    @MessageMapping("/check-fingerprint")
    @SendTo("/topic/attendance")
    public void takeAttendance(String message) {
        System.out.println(message);
    }

    @PostMapping("/series")
    @ResponseBody
    public List<MyObject> postSeries(@RequestBody List<MyObject> myObjects) {
        attendanceService.saveAttendanceSheet(myObjects);
        //myObjects.forEach(System.out::println);
        return myObjects;
    }

}
