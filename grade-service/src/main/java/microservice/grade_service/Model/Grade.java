package microservice.grade_service.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.grade_service.Utils.GradeStatus;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_value")
    private int gradeValue;

    @Column(name = "grade_status")
    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;

    @Column(name = "ordinary_subject_id", nullable = false)
    private Long ordinarySubjectId;

    @Column(name = "elective_subject_id", nullable = false)
    private Long electiveSubjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "student_account_number", nullable = false)
    private String studentAccountNumber;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "school_period", nullable = false)
    private String schoolPeriod;

    @Column(name = "is_authorized", nullable = false)
    private boolean isAuthorized;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "authorized_at")
    private LocalDateTime authorizedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatusAsPending() {
        switch (this.gradeStatus) {
            case NORMAL -> this.gradeStatus = GradeStatus.NORMAL_PENDING;
            case ACCREDITED -> this.gradeStatus = GradeStatus.ACCREDITED_PENDING;
            case  DID_NOT_PRESENT ->  this.gradeStatus = GradeStatus.DID_NOT_PRESENT_PENDING;
        }
    }

    public void removePendingStatus() {
        switch (this.gradeStatus) {
            case NORMAL_PENDING -> this.gradeStatus = GradeStatus.NORMAL;
            case ACCREDITED_PENDING -> this.gradeStatus = GradeStatus.ACCREDITED;
            case  DID_NOT_PRESENT_PENDING ->  this.gradeStatus = GradeStatus.DID_NOT_PRESENT;
        }
    }

    public void setAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    public void setAsNotAuthorized() {
        this.isAuthorized = false;
    }

    public void setAsAuthorized() {
        this.isAuthorized = true;
        this.authorizedAt = LocalDateTime.now();
    }

    public boolean isGradeApproved() {
        return this.gradeValue > 5 || this.gradeStatus == GradeStatus.ACCREDITED;
    }

    public boolean isPending() {
        return !this.isGradeApproved() || this.gradeStatus.isPendingStatus();
    }
}


