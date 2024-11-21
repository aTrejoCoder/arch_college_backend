package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Group.ScheduleDTO;
import microservice.common_classes.Utils.Group.GroupStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public abstract class SubjectInsertDTO {
    @JsonProperty("subject_id")
    @NotNull(message = "subject_id can't be null")
    @Positive(message = "subject_id can't be negative")
    private Long subjectId;

    @JsonProperty("group_status")
    @NotNull(message = "group_status can't be null")
    private GroupStatus groupStatus;

    @JsonProperty("teacher_ids")
    private Set<Long> teacherIds = new HashSet<>();

    @JsonProperty("availableSpots")
    @NotNull(message = "availableSpots can't be null")
    @Positive(message = "availableSpots can't be negative")
    @Min(value = 20, message = "availableSpots can't be below 20")
    @Max(value = 100, message = "availableSpots can't be above 100")
    private int spots;

    @JsonProperty("schedule")
    @NotNull(message = "schedule can't be null")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    @NotNull(message = "classroom can't be null")
    @NotBlank(message = "classroom can't be empty")
    private String classroom;
}
