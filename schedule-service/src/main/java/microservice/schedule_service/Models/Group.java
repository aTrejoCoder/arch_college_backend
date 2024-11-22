package microservice.schedule_service.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Group.GroupStatus;

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

    @Column(name = "obligatory_subject_id", nullable = false)
    private Long obligatorySubjectId;

    @Column(name = "elective_subject_id", nullable = false)
    private Long electiveSubjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "available_spots")
    private int availableSpots;

    @Column(name = "group_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;

    @Column(name = "schoolPeriod", nullable = false)
    private String schoolPeriod;

    @Column(name = "classroom")
    private String classroom;

    @Column(name = "total_spots")
    private int totalSpots;

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

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
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
        this.availableSpots += spotsToAdd;
        this.totalSpots += spotsToAdd;
    }

    public void decreaseAvailableSpot() {
        this.availableSpots--;
    }

    public void increaseAvailableSpot() {
        if (this.availableSpots >= this.totalSpots) {
            throw new RuntimeException("Can't increase available spots above of total spots");
        }
        this.availableSpots++;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void initSpots(int totalSpots) {
        this.totalSpots = totalSpots;
        this.availableSpots = totalSpots;
    }

}
