package microservice.grade_service.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.grade_service.Utils.GroupType;
import microservice.grade_service.Utils.SubjectType;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
public class GradeNamed {
    @JsonProperty("career_id")
    @BsonProperty("career_id")
    private Long careerId;

    @JsonProperty("subject_name")
    @BsonProperty("subject_name")
    private int subjectKey;

    @JsonProperty("subject_credits")
    @BsonProperty("subject_credits")
    private String subjectCredits;

    @JsonProperty("subject_type")
    @BsonProperty("subject_type")
    private SubjectType subjectType;

    @JsonProperty("subject_name")
    @BsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("grade_value")
    @BsonProperty("grade_value")
    private int gradeValue;

    @JsonProperty("group_type")
    @BsonProperty("group_type")
    private GroupType groupType;

    @JsonProperty("school_period")
    @BsonProperty("school_period")
    private String schoolPeriod;

    @JsonProperty("group_key")
    @BsonProperty("group_key")
    private String groupKey;

    @JsonProperty("ordinary_count")
    @BsonProperty("ordinary_count")
    private int ordinaryCount;

    @JsonProperty("extraordinary_count")
    @BsonProperty("extraordinary_count")
    private int extraordinaryCount;

    @JsonProperty("extraordinary_count")
    @BsonProperty("extraordinary_count")
    private boolean isAuthorized;
}
