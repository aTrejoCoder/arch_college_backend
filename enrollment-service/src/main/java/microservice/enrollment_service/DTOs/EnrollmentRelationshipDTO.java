package microservice.enrollment_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRelationshipDTO {
    private List<GradeDTO> studentGrades;
    private StudentDTO studentDTO;
    private GroupDTO groupDTO;
    private ObligatorySubjectDTO obligatorySubjectDTO;
    private ElectiveSubjectDTO electiveSubjectDTO;

    public EnrollmentRelationshipDTO(StudentDTO studentDTO, GroupDTO groupDTO, ObligatorySubjectDTO ordinarySubjectDTO, List<GradeDTO> studentGrades) {
        this.studentGrades = studentGrades;
        this.studentDTO = studentDTO;
        this.groupDTO = groupDTO;
        this.obligatorySubjectDTO = ordinarySubjectDTO;
    }

    public EnrollmentRelationshipDTO(StudentDTO studentDTO, GroupDTO groupDTO, ElectiveSubjectDTO electiveSubjectDTO, List<GradeDTO> studentGrades) {
        this.studentGrades = studentGrades;
        this.studentDTO = studentDTO;
        this.groupDTO = groupDTO;
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

}
