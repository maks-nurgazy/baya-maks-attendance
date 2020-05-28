package project.manas.attendance.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceForm {
    private Boolean marked;
    private String firstName;
    private String lastName;
    private String status;
}
