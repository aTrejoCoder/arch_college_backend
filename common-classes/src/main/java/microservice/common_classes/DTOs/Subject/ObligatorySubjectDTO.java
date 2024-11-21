package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObligatorySubjectDTO extends SubjectDTO {
    @JsonProperty("number")
    private int number;

    @JsonProperty("semester")
    private int semester;

    @JsonProperty("credits")
    private int credits;
}