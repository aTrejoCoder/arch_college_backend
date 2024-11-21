package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ElectiveGroupInsertDTO {
    @JsonProperty("elective_subject_id")
    @NotNull(message = "elective_subject_id can't be null")
    @Positive(message = "elective_subject_id can't be negative")
    private Long electiveSubjectId;

    @JsonProperty("group_status")
    @NotNull(message = "group_status can't be null")
    private GroupStatus groupStatus;

    @JsonProperty("teacher_id")
    private Long teacherId;

    @JsonProperty("availableSpots")
    @NotNull(message = "availableSpots can't be null")
    @Positive(message = "availableSpots can't be negative")
    @Min(value = 20, message = "availableSpots can't be below from 20")
    @Max(value = 100, message = "availableSpots can't be above from 100")
    private int spots;

    @JsonProperty("schedule")
    @NotNull(message = "schedule can't be null")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    @NotNull(message = "classroom can't be null")
    @NotBlank(message = "classroom can't be empty")
    private String classroom;
}
