package project.manas.attendance.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;
import project.manas.attendance.form.SubjectForm;
import project.manas.attendance.model.Role;
import project.manas.attendance.repository.SubjectRepository;
import project.manas.attendance.service.StudentService;
import project.manas.attendance.service.SubjectService;
import project.manas.attendance.service.TeacherService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectRepository subjectRepository;
    private StudentService studentService;
    private TeacherService teacherService;

    @Autowired
    public void setSubjectRepository(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public List<Subject> findSubjectsByTeacherUserName(Principal principal) {
        return subjectRepository.findSubjectByTeacherUserName(principal.getName());
    }

    @Override
    public List<Subject> findSubjectsByTeacherId(int id) {
        return subjectRepository.findSubjectsByTeacherId(id);
    }

    @Override
    public List<Subject> getAllSubject(Role role,Principal principal) {
        switch (role){
            case ADMIN:
                return subjectRepository.findAll();
            case TEACHER:
                return subjectRepository.findSubjectByTeacherUserName(principal.getName());
            case STUDENT:
                return subjectRepository.findSubjectByStudentsIs(studentService.getStudentByUsername(principal.getName()));
        }
        return subjectRepository.findAll();
    }

    @Override
    public void saveSubject(SubjectForm subjectForm) {
        Subject subject = Subject.builder()
                .code(subjectForm.getCode())
                .name(subjectForm.getName())
                .grade(subjectForm.getGrade())
                .build();

        Teacher teacher = teacherService.getTeacherById(Integer.parseInt(subjectForm.getTeacher()));
        teacher.addSubject(subject);
        subjectRepository.save(subject);
    }

    @Override
    public Subject findSubjectById(int id) {
        Optional<Subject> byId = subjectRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public void updateSubject(SubjectForm subjectForm) {

        Subject subject = Subject.builder()
                .code(subjectForm.getCode())
                .name(subjectForm.getName())
                .grade(subjectForm.getGrade())
                .build();

        Teacher teacher = teacherService.getTeacherById(Integer.parseInt(subjectForm.getTeacher()));
        teacher.addSubject(subject);
        subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getAllSubject() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findSubjectByTeacherAndName(Teacher teacher, String subjectName) {
        return subjectRepository.findSubjectByTeacherAndName(teacher, subjectName);
    }



}
