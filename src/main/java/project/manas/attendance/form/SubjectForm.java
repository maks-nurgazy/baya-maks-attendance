package project.manas.attendance.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectForm {
    private String code;
    private String name;
    private int grade;
    private String teacher;
    private int id;
}
