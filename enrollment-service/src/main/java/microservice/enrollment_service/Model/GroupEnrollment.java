package microservice.enrollment_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(name = "subject_key", nullable = false)
    private String subjectKey;

    @Column(name = "group_key", nullable = false)
    private String groupKey;

    @Column(name = "subject_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @Column(name = "subject_credits", nullable = false)
    private int subjectCredits;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    @Column(name = "enrollment_period", nullable = false)
    private String enrollmentPeriod;

    // When Enrollment period ends all enrollments gets locked and can't be modified
    @Column(name = "is_locked", nullable = false)
    private boolean isLocked;


    @PrePersist
    protected void onCreate() {
        this.enrollmentDate = LocalDateTime.now();
        this.isLocked = false;
    }

}
