package microservice.enrollment_service.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class GroupEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "ordinary_subject_id", nullable = false)
    private Long ordinarySubjectId;

    @Column(name = "elective_subject_id", nullable = false)
    private Long electiveSubjectId;

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

    public GroupEnrollment() {
    }

    @PrePersist
    protected void onCreate() {
        var year = LocalDateTime.now().getYear() ;
        int month = LocalDateTime.now().getMonthValue();

        String semester;
        if (month >= 6) {
            semester = "2";
        } else {
            semester = "1";
        }

        this.enrollmentDate = LocalDateTime.now();
        this.enrollmentPeriod = year + "-" + semester;
    }


}
