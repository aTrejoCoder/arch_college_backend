package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObligatorySubjectInsertDTO extends SubjectDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private String name;

    @JsonProperty("area_id")
    @NotNull(message = "area_id can't be null")
    @Positive(message = "area_id must be a positive number")
    private Long areaId;

    @JsonProperty("career_id")
    @NotNull(message = "career_id can't be null")
    @Positive(message = "career_id must be a positive number")
    private Long careerId;

    @JsonProperty("number")
    @NotNull(message = "number can't be null")
    @Positive(message = "number must be a positive number")
    @Max(value = 10, message = "number can't be greater than 10")
    private int number;

    @JsonProperty("semester")
    @NotNull(message = "semester can't be null")
    @Positive(message = "semester must be a positive")
    @Max(value = 10, message = "semester can't be greater than 10")
    private int semester;

    @JsonProperty("credits")
    @NotNull(message = "credits can't be null")
    @Positive(message = "credits must be a positive number")
    private int credits;
}