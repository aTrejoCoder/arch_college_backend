package microservice.subject_service.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdinarySubjectInsertDTO {
    @JsonProperty("name")
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private String name;

    @JsonProperty("area_id")
    @NotNull(message = "area_id can't be null")
    @Positive(message = "area_id can't be negative")
    private Long areaId;

    @JsonProperty("semester_number")
    @NotNull(message = "semester_number can't be null")
    @Positive(message = "semester_number can't be negative")
    @Size(max = 10, message = "semester_number can't be above 10")
    private int semesterNumber;

    @JsonProperty("credits")
    @NotNull(message = "credits can't be null")
    @Positive(message = "credits can't be negative")
    private int credits;
}
