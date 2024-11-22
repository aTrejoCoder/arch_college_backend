package microservice.enrollment_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
