package microservice.grade_service.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupType;
import microservice.common_classes.Utils.SubjectType;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
public class GradeNamed {
    @JsonProperty("career_id")
    @BsonProperty("career_id")
    private Long careerId;

    @BsonProperty("subject_key")
    private int subjectKey;

    @BsonProperty("subject_credits")
    private String subjectCredits;

    @BsonProperty("subject_type")
    private SubjectType subjectType;

    @BsonProperty("subject_name")
    private String subjectName;

    @BsonProperty("grade_value")
    private int gradeValue;

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

    @JsonProperty("is_authorized")
    @BsonProperty("is_authorized")
    private boolean isAuthorized;
}
