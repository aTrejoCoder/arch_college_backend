package microservice.enrollment_service.Model.Preload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "obligatory_subject")
public class ObligatorySubject {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("key")
    private String key;

    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("number")
    private int number;

    @JsonProperty("semester")
    private int semester;

    @JsonProperty("credits")
    private int credits;
}
