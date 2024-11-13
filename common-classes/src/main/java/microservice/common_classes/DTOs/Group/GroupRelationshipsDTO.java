package microservice.common_classes.DTOs.Group;

import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;

@Data
@NoArgsConstructor
public class GroupRelationshipsDTO {

    private TeacherDTO teacherDTO;
    private OrdinarySubjectDTO ordinarySubjectDTO;
    private ElectiveSubjectDTO electiveSubjectDTO;

    public GroupRelationshipsDTO(TeacherDTO teacherDTO, OrdinarySubjectDTO ordinarySubjectDTO) {
        this.teacherDTO = teacherDTO;
        this.ordinarySubjectDTO = ordinarySubjectDTO;
    }

    public GroupRelationshipsDTO(OrdinarySubjectDTO ordinarySubjectDTO) {
        this.ordinarySubjectDTO = ordinarySubjectDTO;
    }

    public GroupRelationshipsDTO(TeacherDTO teacherDTO, ElectiveSubjectDTO electiveSubjectDTO) {
        this.teacherDTO = teacherDTO;
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

    public GroupRelationshipsDTO(TeacherDTO teacherDTO) {
        this.teacherDTO = teacherDTO;
    }


    public GroupRelationshipsDTO(ElectiveSubjectDTO electiveSubjectDTO) {
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

}
