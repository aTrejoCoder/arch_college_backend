package microservice.enrollment_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.enrollment_service.Model.Preload.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRelationship {
    private List<Grade> studentGrades;
    private Student student;
    private Group group;
    private ObligatorySubject obligatorySubject;
    private ElectiveSubject electiveSubject;

        public EnrollmentRelationship(Student student, Group group, ObligatorySubject obligatorySubject, List<Grade> studentGrades) {
        this.studentGrades = studentGrades;
        this.student = student;
        this.group = group;
        this.obligatorySubject = obligatorySubject;
    }

        public EnrollmentRelationship(Student student, Group group, ElectiveSubject electiveSubject, List<Grade> studentGrades) {
        this.studentGrades = studentGrades;
        this.student = student;
        this.group = group;
        this.electiveSubject = electiveSubject;
    }

}
