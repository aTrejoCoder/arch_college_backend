package microservice.common_classes.DTOs.Enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EnrollmentDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("enrollment_date")
    private LocalDateTime enrollmentDate;
}
