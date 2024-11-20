package microservice.common_classes.DTOs.Enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @JsonProperty("student_id")
    @NotNull(message = "student_id can't be null")
    @Positive(message = "student_id cant' be negative")
    private Long studentId;
}