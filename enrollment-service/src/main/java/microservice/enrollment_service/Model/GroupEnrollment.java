package microservice.enrollment_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.GroupType;
import microservice.common_classes.Utils.SubjectType;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "enrollment")
public class GroupEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_key", nullable = false)
    private String groupKey;

    @Column(name = "subject_key", nullable = false)
    private String subjectKey;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "subject_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @Column(name = "student_account_number", nullable = false)
    private String studentAccountNumber;

    @Column(name = "subject_credits", nullable = false)
    private int subjectCredits;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    @Column(name = "enrollment_period", nullable = false)
    private String enrollmentPeriod;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public void softDeleteEnrollment() {
        this.isActive = false;
    }

    @PrePersist
    protected void onCreate() {
        this.enrollmentDate = LocalDateTime.now();
        this.isActive = Boolean.TRUE;
    }

}
