package microservice.subject_service.DTOs.ProfessionalLine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalLineDTO {
    @JsonProperty("professional_line_id")
    private Long professionalLineId;

    @JsonProperty("name")
    private String name;
}
