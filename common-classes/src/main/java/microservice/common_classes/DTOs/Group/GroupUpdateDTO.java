package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import microservice.common_classes.Utils.Schedule.WEEKDAY;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupUpdateDTO {
    @JsonProperty("group_id")
    @NotNull(message = "group_id can't be null")
    @Positive(message = "group_id can't be negative")
    private Long group_id;

    @JsonProperty("teacher_id")
    private Long teacherId;

    @JsonProperty("clear_teacher")
    private boolean removeTeacher = false;

    @JsonProperty("spots")
    private int spotsToAdd = 0;

    @JsonProperty("schedule")
    private List<ScheduleDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;
}
