package microservice.schedule_service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("teacherId")
    private Long teacherId;

    @JsonProperty("spots")
    private int spots;

    @JsonProperty("schedule")
    private List<ScheduleInsertDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;
}
