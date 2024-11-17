package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectiveSubjectDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("professional_line_id")
    private Long professionalLineId;

}
