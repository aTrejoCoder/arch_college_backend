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

    @Column(name = "subject_id", nullable = false)
    private Long subject_id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

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
