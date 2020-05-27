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
import project.manas.attendance.form.SubjectForm;
import project.manas.attendance.model.FingerprintStatus;
import project.manas.attendance.model.Role;
import project.manas.attendance.service.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    StatisticService statisticService;
    TeacherService teacherService;
    SubjectService subjectService;
    StudentService studentService;
    FingerprintService fingerprintService;


    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setFingerprintService(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("statistic", statisticService.getStatistic());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("statistic", statisticService.getStatistic());
        return "index";
    }

    @GetMapping("/all-teacher")
    public String allTeacher(Model model) {
        List<Teacher> allTeacher = teacherService.getAllTeacher();
        model.addAttribute("teacherList", allTeacher);
        return "all-teacher";
    }

    @GetMapping("/add-teacher")
    public String addTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "add-teacher";
    }

    @PostMapping("/add-teacher")
    public String addTeacher(@RequestParam("teacherId") int id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        return "add-teacher";
    }

    @PostMapping("/saveTeacher")
    public String saveTeacher(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "redirect:/admin/all-teacher";
    }

    @PostMapping("/deleteTeacher")
    public String delete(@RequestParam("teacherId") int id) {
        teacherService.deleteTeacher(id);
        return "redirect:/admin/all-teacher";
    }

    @GetMapping("/teacher-details")
    public String teacherDetails(@RequestParam("teacherId") int id, Model model) {
        Teacher teacherDetails = teacherService.getTeacherById(id);
        StringBuilder subjectList = new StringBuilder();
        for (Subject subject : teacherDetails.getSubjects()) {
            subjectList.append(subject.getName()).append(", ");
        }
        model.addAttribute("teacherDetails", teacherDetails);
        model.addAttribute("subjectList", subjectList.toString());
        return "teacher-details";
    }

    @GetMapping("/all-subject")
    public String allSubject(Model model, Principal principal) {
        List<Subject> allSubject = subjectService.getAllSubject(Role.ADMIN, principal);
        List<Teacher> allTeacher = teacherService.getAllTeacherOrderByFirstName();
        model.addAttribute("subjectList", allSubject);
        model.addAttribute("teacherList", allTeacher);
        model.addAttribute("mSubject",new SubjectForm());
        return "all-subject";
    }

    @PostMapping("/save-subject")
    public String saveSubject(@ModelAttribute("mSubject") SubjectForm subjectForm){
        subjectService.saveSubject(subjectForm);
        return "redirect:/admin/all-subject";
    }

    @GetMapping("/update-subject")
    public String updateSubject(@RequestParam("subjectId") int id, Model model,Principal principal) {
        List<Subject> allSubject = subjectService.getAllSubject(Role.ADMIN, principal);
        List<Teacher> allTeacher = teacherService.getAllTeacherOrderByFirstName();
        model.addAttribute("subjectList", allSubject);
        model.addAttribute("teacherList", allTeacher);
        Subject subject = subjectService.findSubjectById(id);
        SubjectForm subjectForm = SubjectForm.builder()
                .code(subject.getCode())
                .grade(subject.getGrade())
                .name(subject.getName()).build();
        Teacher t = subject.getTeacher();
        subjectForm.setTeacher(t.getFirstName()+" "+t.getLastName());
        subjectForm.setId(t.getId());
        model.addAttribute("mSubject",subjectForm);
        return "all-subject";
    }

    @PostMapping("/update-subject")
    public String saveUpdatedSubject(@ModelAttribute("mSubject") SubjectForm subjectForm){
        subjectService.updateSubject(subjectForm);
        return "redirect:/admin/all-subject";
    }

    @GetMapping("/all-student")
    public String allStudent(Model model) {
        List<Student> allStudent = studentService.getAllStudent();
        List<Student> halfStudent = new ArrayList<>(allStudent.subList(0, allStudent.size() / 50));
        model.addAttribute("studentList", halfStudent);
        return "all-student";
    }

    @GetMapping("/admit-form")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "admit-form";
    }

    @PostMapping("/add-student")
    public String addStudent(@RequestParam("studentId") String id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "admit-form";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/admin/all-student";
    }

    @PostMapping("/deleteStudent")
    public String delete(@RequestParam("studentId") String id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/all-student";
    }

    @GetMapping("/student-details")
    public String studentDetails(@RequestParam("studentId") String id, Model model) {
        StringBuilder subjectList = new StringBuilder();
        Student studentDetails = studentService.getStudentById(id);
        for (Subject subject : studentDetails.getSubjects()) {
            subjectList.append(subject.getName()).append(", ");
        }
        model.addAttribute("studentDetails", studentDetails);
        model.addAttribute("subjectList", subjectList.toString());
        return "student-details";
    }

    @GetMapping("/students")
    public String studentList(@PathParam("subjectId") Integer subjectId, Model model) {
        List<Student> students = teacherService.getStudentList(subjectId);
        model.addAttribute("studentList", students);
        return "fragment/fingerprint :: student-list";
    }

    @GetMapping("/student-fingerprint")
    public String studentFingerprintForm(Model model) {
        List<Subject> subjectList = subjectService.getAllSubject();
        model.addAttribute("subjectList",subjectList);
        return "add-fingerprint";
    }

    @MessageMapping("/new-fingerprint")
    @SendTo("/topic/fingerprints")
    public FingerprintStatus registerFingerprints(String message) {
        fingerprintService.handleMessage(message);
        return null;
    }

}

