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
    @JsonProperty("group_key")
    @NotNull(message = "group_key can't be null")
    @Positive(message = "group_key cant' be negative")
    private String groupKey;

    @JsonProperty("subject_key")
    @NotNull(message = "subject_key can't be null")
    @Positive(message = "subject_key cant' be negative")
    private String subjectKey;

}