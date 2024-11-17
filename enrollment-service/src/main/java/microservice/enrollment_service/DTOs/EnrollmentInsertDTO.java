package microservice.enrollment_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EnrollmentInsertDTO {
    @JsonProperty("group_id")
    @NotNull(message = "group_id can't be null")
    @Positive(message = "group_id cant' be negative")
    private Long groupId;

    @JsonProperty("student_account_number")
    @NotNull(message = "student_account_number can't be null")
    @Positive(message = "student_account_number cant' be negative")
    private String studentAccountNumber;
}
