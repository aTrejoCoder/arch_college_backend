package microservice.common_classes.DTOs.Subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class SubjectDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("serial_number")
    private int serialNumber;

    @JsonProperty("name")
    private String name;

    @JsonProperty("key")
    private String key;

    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("subject_series_id")
    private Long seriesId;
}
