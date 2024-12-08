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
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_account_number")
    private String studentAccountNumber;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_key")
    private String subjectKey;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_credits")
    private int subjectCredits;

    @Column(name = "subject_type")
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_key")
    private String groupKey;

    @Column(name = "school_period")
    private String schoolPeriod;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    // When Enrollment period ends all enrollments gets locked and can't be modified
    @Column(name = "is_locked")
    private boolean isLocked;

    public void lock () {
        this.isLocked = true;
    }

    @PrePersist
    protected void onCreate() {
        this.enrollmentDate = LocalDateTime.now();
        this.isLocked = false;
    }

}
