package microservice.common_classes.DTOs.Teacher;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.Utils.Teacher.Title;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherNameDTO {
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "teacher_name")
    private String teacherName;
}