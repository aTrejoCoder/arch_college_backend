package microservice.grade_service.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.grade_service.Utils.GradeStatus;
import org.checkerframework.checker.units.qual.N;

@Data
@NoArgsConstructor
public class GradeInsertDTO {
    @JsonProperty("group_id")
    @NotNull(message = "group_id can't be null")
    @Positive(message = "group_id cant' be negative")
    private Long groupId;

    @JsonProperty("grade_value")
    @Positive(message = "grade_value can't be positive")
    private int gradeValue;

    @JsonProperty("grade_status")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "grade_status can't be null")
    private GradeStatus gradeStatus;

    @JsonProperty("ordinary_subject_id")
    private Long ordinarySubjectId;

    @JsonProperty("elective_subject_id")
    private Long electiveSubjectId;

    @JsonProperty("student_account_number")
    @NotNull(message = "student_account_number can't be null")
    @NotBlank(message = "student_account_number cant' be empty")
    private String studentAccountNumber;
}
