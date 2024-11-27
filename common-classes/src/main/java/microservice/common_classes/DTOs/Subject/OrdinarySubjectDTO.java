package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdinarySubjectDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("number")
    private int number;

    @JsonProperty("semester_number")
    private int semesterNumber;

    @JsonProperty("credits")
    private int credits;

}