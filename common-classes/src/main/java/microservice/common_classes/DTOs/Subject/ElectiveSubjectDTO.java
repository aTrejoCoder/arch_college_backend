package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElectiveSubjectDTO extends SubjectDTO {
    @JsonProperty("professional_line_id")
    private Long professionalLineId;
}