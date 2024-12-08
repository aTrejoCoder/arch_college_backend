package microservice.common_classes.DTOs.ProfessionalLine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalLineDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
}