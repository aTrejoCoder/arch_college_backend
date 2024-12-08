package microservice.grade_service.DTOs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import microservice.grade_service.Model.Grade;


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
    private Grade.GradeResult gradeResult;

    @Column(name = "grade_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade.GradeStatus gradeStatus;

}
