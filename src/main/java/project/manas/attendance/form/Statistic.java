package project.manas.attendance.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistic {
    private int nStudent;
    private int nTeacher;
    private int nGirls;
    private int nBoys;
}
