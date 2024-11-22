package microservice.grade_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Subject.SubjectDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRelationshipsDTO {
    private StudentDTO studentDTO;
    private SubjectDTO subjectDTO;
    private GroupDTO groupDTO;
    private ProfessionalLineDTO professionalLineDTOS;
}
