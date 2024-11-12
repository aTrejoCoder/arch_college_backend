package microservice.schedule_service.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupRelationshipsDTO;
import microservice.common_classes.Utils.Schedule.SemesterData;

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

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "teacherId")
    private Long teacherId;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "key")
    private String key;

    @Column(name = "spots", nullable = false)
    private int spots;

    @Column(name = "schoolPeriod", nullable = false)
    private String schoolPeriod;

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
