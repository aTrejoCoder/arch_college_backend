package microservice.schedule_service.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Teacher.Title;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teacher")
@Data
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "teacher_title")
    @Enumerated(EnumType.STRING)
    private Title teacherTitle;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "teachers_schedule",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    )
        private List<Schedule> schedules = new ArrayList<>();

    // +John Doe, ARCH
    public String composeNameWithTitle() {
        return  "+" + teacherName + "," + teacherTitle.getInitials();
    }

}