package microservice.schedule_service.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.GroupStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "spots")
    private int spots;

    @Column(name = "group_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;

    @Column(name = "schoolPeriod", nullable = false)
    private String schoolPeriod;

    @Column(name = "classroom")
    private String classroom;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_schedule",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<Schedule> schedule = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_teacher",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addTeachers(List<Teacher> teachers) {
            this.teachers.addAll(teachers);
    }

    public void removeTeacher(Teacher teacher) {
        this.getTeachers().remove(teacher);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    
    public void increaseSpots(int spotsToAdd) {
        this.spots += spotsToAdd;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
