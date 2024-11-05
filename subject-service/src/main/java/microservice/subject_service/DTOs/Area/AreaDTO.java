package microservice.subject_service.DTOs.Area;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.subject_service.Model.ElectiveSubject;
import microservice.subject_service.Model.OrdinarySubject;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class  AreaDTO {
    @JsonProperty("area_id")
    private Long areaId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
