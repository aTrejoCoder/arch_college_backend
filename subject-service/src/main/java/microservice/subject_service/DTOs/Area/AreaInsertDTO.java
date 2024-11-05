package microservice.subject_service.DTOs.Area;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AreaInsertDTO {

    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private String name;
}
