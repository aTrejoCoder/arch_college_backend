package microservice.schedule_service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.schedule_service.Utils.WEEKDAY;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupUpdateDTO {
    @JsonProperty("group_id")
    @NotNull(message = "group_id can't be null")
    @Positive(message = "group_id can't be negative")
    private Long group_id;

    @JsonProperty("subject_id")
    @NotNull(message = "subject_id can't be null")
    @Positive(message = "subject_id can't be negative")
    private Long subjectId;

    @JsonProperty("teacher_id")
    @NotNull(message = "teacher_id can't be null")
    @Positive(message = "teacher_id can't be negative")
    private Long teacherId;

    @JsonProperty("spots")
    @NotNull(message = "spots can't be null")
    @Positive(message = "spots can't be negative")
    private int spots;

    @JsonProperty("day")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "day can't be null")
    private WEEKDAY day;

    @JsonProperty("schedule")
    @NotNull(message = "schedule can't be null")
    private List<ScheduleInsertDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;
}
