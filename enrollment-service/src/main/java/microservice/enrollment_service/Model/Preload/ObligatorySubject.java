package microservice.enrollment_service.Model.Preload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "obligatory_subject")
public class ObligatorySubject {
    @JsonProperty("id")
    @Id
    @Field("_id")
    private Long id;

    @JsonProperty("name")
    @Field("name")
    private String name;

    @JsonProperty("key")
    @Field("key")
    private String key;

    @JsonProperty("area_id")
    @Field("areaId")
    private Long areaId;

    @JsonProperty("number")
    @Field("number")
    private int number;

    @JsonProperty("semester")
    @Field("semester")
    private int semester;

    @JsonProperty("credits")
    @Field("credits")
    private int credits;
}
