package project.manas.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import project.manas.attendance.service.AttendanceService;
import project.manas.attendance.service.FingerprintService;

import java.io.IOException;

@Controller
public class ArduinoController {

    FingerprintService fingerprintService;
    AttendanceService attendanceService;

    @Autowired
    public void setFingerprintService(FingerprintService fingerprintService) {
        this.fingerprintService = fingerprintService;
    }

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/arduino-fingerprint")
    @ResponseBody
    public String arduinoFingerprint(@RequestParam("fingerprint") MultipartFile fingerprintFile) {
        try {
            byte[] fingerprintByte = fingerprintFile.getBytes();
            return  fingerprintService.handleImage(fingerprintByte);
        } catch (IOException e) {
            return "Failure... Send again";
        }
    }

    @GetMapping("/arduino-fingerprint1")
    @ResponseBody
    public String arduinoFingerprint1(@RequestParam("fingerprint") MultipartFile fingerprintFile) {
        try {
            byte[] fingerprintByte = fingerprintFile.getBytes();
            return  attendanceService.handleImage(fingerprintByte);
        } catch (IOException e) {
            return "Failure... Send again";
        }
    }

}
