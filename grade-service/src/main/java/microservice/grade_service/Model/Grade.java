package microservice.grade_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Grades.GradeResult;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.Grades.GradeType;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_value")
    private Integer gradeValue;

    @Column(name = "student_account_number", nullable = false)
    private String studentAccountNumber;

    @Column(name = "grade_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    @Column(name = "grade_result")
    @Enumerated(EnumType.STRING)
    private GradeResult gradeResult;

    @Column(name = "school_period", nullable = false)
    private String schoolPeriod;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "grade_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "authorized_at")
    private LocalDateTime authorizedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void setInitialValues(GradeType gradeType) {
        this.gradeStatus = GradeStatus.PENDING_RESULT;
        this.gradeType =  gradeType;
    }

    public void setAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    public void setAsAuthorized() {
        this.authorizedAt = LocalDateTime.now();
        this.gradeStatus = GradeStatus.VALIDATED;
    }

    public void setAsNotPresent() {
        this.gradeResult = GradeResult.NOT_PRESENTED;
        this.gradeStatus = GradeStatus.PENDING_VALIDATION;
    }

    public void rate(int gradeValue) {
        if (this.gradeStatus != GradeStatus.PENDING_RESULT) {
            throw new IllegalStateException("Cannot rate a grade that is not in PENDING_RESULT status.");
        }

        if (this.deletedAt != null) {
            throw new IllegalStateException("Cannot rate a deleted grade.");
        }

        if (gradeType == GradeType.CREDITABLE) {
            this.gradeValue = gradeValue;
        }

        this.gradeResult = gradeValue > 5 ? GradeResult.APPROVED : GradeResult.NOT_APPROVED;
        this.gradeStatus = GradeStatus.PENDING_VALIDATION;
    }

    public void setAsNotRated() {
        this.gradeValue = null;
        this.gradeResult = null;
        this.gradeStatus = GradeStatus.PENDING_RESULT;
        this.updatedAt = this.createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}


