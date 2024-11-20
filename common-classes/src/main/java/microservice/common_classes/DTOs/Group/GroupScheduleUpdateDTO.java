package microservice.common_classes.DTOs.Group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import microservice.common_classes.Utils.Group.GroupStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupScheduleUpdateDTO {
    @JsonProperty("group_id")
    @NotNull(message = "group_id can't be null")
    @Positive(message = "group_id can't be negative")
    private Long group_id;

    @JsonProperty("schedule")
    private List<ScheduleDTO> schedule;

    String classroom;
    @JsonProperty("group_status")
    @NotNull(message = "group_status can't be null")
    private GroupStatus groupStatus;
}
