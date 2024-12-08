package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ObligatoryGroupInsertDTO {
    @JsonProperty("subject_id")
    @NotNull(message = "subject_id can't be null")
    @Positive(message = "subject_id can't be negative")
    private Long subjectId;

    @JsonProperty("group_status")
    @NotNull(message = "group_status can't be null")
    private GroupStatus groupStatus;

    @JsonProperty("teacher_ids")
    @NotNull(message = "teacher_ids can't be null")
    private Set<Long> teacherIds = new HashSet<>();

    @JsonProperty("head_teacher_id")
    @Positive(message = "head_teacher_id can't be negative")
    private Long headTeacherId;

    @JsonProperty("total_spots")
    @NotNull(message = "total_spots can't be null")
    @Positive(message = "total_spots can't be negative")
    @Min(value = 20, message = "total_spots can't be below from 20")
    @Max(value = 100, message = "total_spots can't be above from 100")
    private int totalSpots;

    @JsonProperty("schedule")
    @NotNull(message = "schedule can't be null")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    @NotNull(message = "classroom can't be null")
    @NotBlank(message = "classroom can't be empty")
    private String classroom;
}
