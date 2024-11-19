package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdinarySubjectDTO extends SubjectDTO {
    @JsonProperty("number")
    private int number;

    @JsonProperty("semester_number")
    private int semesterNumber;

    @JsonProperty("credits")
    private int credits;
}