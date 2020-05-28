package project.manas.attendance.service;


import project.manas.attendance.entity.Subject;
import project.manas.attendance.model.MyObject;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

public interface AttendanceService {
    List<Subject> getTeacherSubjects(Principal principal);
    void setSubjectId(Integer subjectId);
    String handleImage(byte[] fingerprintByte);
    void saveAttendanceSheet(List<MyObject> myObjects);
}
