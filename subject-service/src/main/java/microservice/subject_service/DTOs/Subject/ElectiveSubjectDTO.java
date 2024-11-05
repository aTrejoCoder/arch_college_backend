package microservice.subject_service.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectiveSubjectDTO {
    @JsonProperty("elective_subject_id")
    private Long electiveSubjectId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("professional_line_id")
    private Long professionalLineId;
}
