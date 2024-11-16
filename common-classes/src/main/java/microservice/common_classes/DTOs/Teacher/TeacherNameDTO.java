package microservice.common_classes.DTOs.Teacher;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Title;

@Data
@NoArgsConstructor
public class TeacherNameDTO {
    private Long teacherId;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "teacher_title")
    @Enumerated(EnumType.STRING)
    private Title teacherTitle;
}