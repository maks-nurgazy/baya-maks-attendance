package project.manas.attendance.service;



import project.manas.attendance.entity.Subject;
import project.manas.attendance.entity.Teacher;
import project.manas.attendance.form.SubjectForm;
import project.manas.attendance.model.Role;

import java.security.Principal;
import java.util.List;

public interface SubjectService {
    Subject findSubjectByTeacherAndName(Teacher teacher, String subjectName);
    List<Subject> findSubjectsByTeacherUserName(Principal principal);
    List<Subject> findSubjectsByTeacherId(int id);
    List<Subject> getAllSubject(Role role,Principal principal);
    void saveSubject(SubjectForm subjectForm);
    Subject findSubjectById(int id);
    void updateSubject(SubjectForm subjectForm);
    List<Subject> getAllSubject();
}
