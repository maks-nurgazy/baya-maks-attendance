package project.manas.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FingerprintStatus {
    private int number;
    private String status;
    private String id;

    public FingerprintStatus(int number, String status) {
        this.number = number;
        this.status = status;
    }
}
