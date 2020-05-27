package project.manas.attendance.service.impl;

import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Fingerprint;
import project.manas.attendance.entity.Student;
import project.manas.attendance.model.FingerprintStatus;
import project.manas.attendance.service.FingerprintService;
import project.manas.attendance.service.StudentService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class FingerprintServiceImpl implements FingerprintService {
    private String studentId;
    private List<byte[]> fingerprintBuffer;
    private SimpMessageSendingOperations messagingTemplate;
    private StudentService studentService;


    @Autowired
    public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostConstruct
    public void init() {
        fingerprintBuffer = new ArrayList<>();
        studentId = "";
    }

    @Override
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String addFingerprintToBuffer(byte[] fingerprintByte) {
        if (!isStudentChecked()) {
            return "Choose Student first! Try again...";
        }

        if (fingerprintBuffer.size() < 6) {
            fingerprintBuffer.add(fingerprintByte);
        } else {
            FingerprintStatus status = FingerprintStatus.builder()
                    .number(fingerprintBuffer.size())
                    .status("full").build();
            messagingTemplate.convertAndSend("/topic/fingerprints", status);
        }
        FingerprintStatus status = FingerprintStatus.builder()
                .number(fingerprintBuffer.size())
                .status("added").build();
        messagingTemplate.convertAndSend("/topic/fingerprints", status);
        return "added to buffer";
    }

    @Override
    public void clearFingerprintBuffer() {
        fingerprintBuffer.clear();
    }

    @Override
    public void handleMessage(String message) {
        switch (message) {
            case "disconnect":
                clearFingerprintBuffer();
                setStudentId("");
                break;
            case "connect":
                System.out.println("Connect");
                break;
            case "save":
                if (fingerprintBuffer.size() == 0) {
                    messagingTemplate.convertAndSend("/topic/fingerprints", new FingerprintStatus(0, "empty"));
                } else {
                    saveBufferedStudentFingerprint();
                    messagingTemplate.convertAndSend("/topic/fingerprints", new FingerprintStatus(fingerprintBuffer.size(), "saved", studentId));
                }
                break;
            default:
                studentId = message;
                clearFingerprintBuffer();
                System.out.println(studentId);
                break;
        }
    }

    @Override
    public String handleImage(byte[] fingerprintByte) {
        FingerprintTemplate template = new FingerprintTemplate(
                new FingerprintImage()
                        .dpi(500)
                        .decode(fingerprintByte));
        byte[] serialized = template.toByteArray();
        return addFingerprintToBuffer(serialized);
    }

    private boolean isStudentChecked() {
        return !studentId.equals("");
    }

    private void saveBufferedStudentFingerprint() {
        Student student = studentService.getStudentById(studentId);
        List<Fingerprint> fingerprints = new ArrayList<>();
        for (byte[] fByte : fingerprintBuffer) {
            Fingerprint fingerprint = Fingerprint.builder()
                    .sample(fByte)
                    .student(student)
                    .build();
            fingerprints.add(fingerprint);
        }
        student.setFingerprints(fingerprints);
        studentService.saveUpdatedStudent(student);
    }
}
