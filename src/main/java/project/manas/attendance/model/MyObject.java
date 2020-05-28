package project.manas.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyObject {

    private String stId;
    private String firstName;
    private String lastName;
    private Status status;
    private Integer subjectId;
    private String subjectType;
    private String date;

}