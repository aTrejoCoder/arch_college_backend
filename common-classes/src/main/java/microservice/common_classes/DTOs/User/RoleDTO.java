package microservice.common_classes.DTOs.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RoleDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
}