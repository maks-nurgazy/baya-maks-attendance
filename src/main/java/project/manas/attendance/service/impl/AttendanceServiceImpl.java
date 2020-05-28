package project.manas.attendance.service.impl;


import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.*;
import project.manas.attendance.model.MyObject;
import project.manas.attendance.model.Status;
import project.manas.attendance.repository.AttendanceRepository;
import project.manas.attendance.service.AttendanceService;
import project.manas.attendance.service.FingerCompareService;
import project.manas.attendance.service.StudentService;
import project.manas.attendance.service.SubjectService;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    SubjectService subjectService;
    Integer subjectId = -1;
    FingerCompareService fingerCompareService;
    private SimpMessageSendingOperations messagingTemplate;
    private StudentService studentService;
    private AttendanceRepository attendanceRepository;

    @Autowired
    public void setAttendanceRepository(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    public void setFingerCompareService(FingerCompareService fingerCompareService) {
        this.fingerCompareService = fingerCompareService;
    }

    @Autowired
    public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @Override
    public List<Subject> getTeacherSubjects(Principal principal) {
        return subjectService.findSubjectsByTeacherUserName(principal);
    }

    @Override
    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
        notifyDatasetChanged(subjectId);
    }

    private void notifyDatasetChanged(Integer subjectId) {
        Subject subject = subjectService.findSubjectById(subjectId);
        List<Student> students = subject.getStudents();
        List<Fingerprint> fingerprints = new ArrayList<>();
        for (Student student : students) {
            List<Fingerprint> fingers = student.getFingerprints();
            fingerprints.addAll(fingers);
        }
        fingerCompareService.setCandidates(fingerprints);
    }

    @Override
    public String handleImage(byte[] fingerprintByte) {
        FingerprintTemplate template = new FingerprintTemplate(
                new FingerprintImage()
                        .dpi(500)
                        .decode(fingerprintByte));
        byte[] serialized = template.toByteArray();
        Fingerprint fingerprint = fingerCompareService.compare(serialized);
        if (fingerprint != null) {
            String id = fingerprint.getStudent().getId();
            messagingTemplate.convertAndSend("/topic/attendance", id);
            return "Success";
        }
        return "Failure";
    }

    @Override
    public void saveAttendanceSheet(List<MyObject> myObjects){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        List<Record> attendanceRecord = new ArrayList<>();

        Date date = null;
        try {
            date = formatter.parse(myObjects.get(0).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Subject subject = subjectService.findSubjectById(myObjects.get(0).getSubjectId());

        Attendance attendance = Attendance.builder()
                .date(date)
                .subject(subject)
                .build();

        for (MyObject myObject : myObjects) {
            Student student = studentService.getStudentById(myObject.getStId());
            Status status = myObject.getStatus();

            Record record = Record.builder()
                    .student(student)
                    .status(status)
                    .attendance(attendance)
                    .build();
            attendanceRecord.add(record);
        }

        attendance.setRecords(attendanceRecord);
        attendanceRepository.save(attendance);
    }
}
