package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupInsertDTO {

    @JsonProperty("ordinary_subject_id")
    private Long ordinarySubjectId;

    @JsonProperty("elective_subject_id")
    private Long electiveSubjectId;

    @JsonProperty("teacher_id")
    @NotNull(message = "teacher_id can't be null")
    @Positive(message = "teacher_id can't be negative")
    private Long teacherId;

    @JsonProperty("spots")
    @NotNull(message = "spots can't be null")
    @Positive(message = "spots can't be negative")
    @Min(value = 20, message = "spots can't be below from 20")
    @Max(value = 100, message = "spots can't be above from 100")
    private int spots;

    @JsonProperty("schedule")
    @NotNull(message = "schedule can't be null")
    private List<ScheduleInsertDTO> schedule;

    @JsonProperty("classroom")
    @NotNull(message = "classroom can't be null")
    @NotBlank(message = "classroom can't be empty")
    private String classroom;
}
