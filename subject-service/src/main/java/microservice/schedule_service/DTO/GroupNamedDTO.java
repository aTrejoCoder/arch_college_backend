package microservice.schedule_service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupNamedDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("teacher_name")
    private String teacherName;

    @JsonProperty("spots")
    private int spots;

    @JsonProperty("schedule")
    private List<ScheduleInsertDTO> schedule;

    @JsonProperty("classroom")
    private String classroom;
}