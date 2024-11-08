package microservice.schedule_service.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "\"group\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ordinary_subject_id", nullable = false)
    private Long ordinarySubjectId;

    @Column(name = "elective_subject_id", nullable = false)
    private Long electiveSubjectId;

    @Column(name = "teacherId")
    private Long teacherId;

    @Column(name = "key")
    private String key;

    @Column(name = "spots", nullable = false)
    private int spots;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "classroom")
    private String classroom;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_schedule",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<Schedule> schedule;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Semester Format is 2024-2 --> 2024 means year - 2 means the
    // number of the semester in the year (January - July = 1) (August - December = 2)
    public void setCurrentSemester() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int currentMonth = now.getMonthValue();

        int semesterNumber;
        if (currentMonth < 6) {
            semesterNumber = 1;
        } else  {
            semesterNumber = 2;
        }

        this.semester = year + "-" + semesterNumber;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
