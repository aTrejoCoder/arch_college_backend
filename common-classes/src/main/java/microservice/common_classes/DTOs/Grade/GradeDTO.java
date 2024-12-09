package microservice.common_classes.DTOs.Grade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import microservice.common_classes.Utils.Grades.GradeResult;
import microservice.common_classes.Utils.Grades.GradeStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {
    @Column(name = "student_account_number", nullable = false)
    private String studentAccountNumber;

    @Column(name = "grade_value")
    private Integer gradeValue;

    @Column(name = "grade_result")
    @Enumerated(EnumType.STRING)
    private GradeResult gradeResult;

    @Column(name = "grade_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;
}
