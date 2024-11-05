package microservice.subject_service.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdinarySubjectDTO {
    @JsonProperty("ordinary_subject_id")
    private Long ordinarySubjectId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("semester_number")
    private int semesterNumber;

    @JsonProperty("credits")
    private int credits;
}
