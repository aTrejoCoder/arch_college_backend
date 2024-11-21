package microservice.schedule_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;


import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupRelationshipsDTO {

    private List<TeacherDTO> teacherDTOS;
    private ObligatorySubjectDTO obligatorySubjectDTO;
    private ElectiveSubjectDTO electiveSubjectDTO;

    public GroupRelationshipsDTO(ElectiveSubjectDTO electiveSubjectDTO, TeacherDTO teacherDTO) {
        this.teacherDTOS = Collections.singletonList(teacherDTO);
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

        public GroupRelationshipsDTO(ObligatorySubjectDTO obligatorySubjectDTO, List<TeacherDTO> teacherDTOS) {
        this.obligatorySubjectDTO = obligatorySubjectDTO;
        this.teacherDTOS = teacherDTOS;
    }

    public GroupRelationshipsDTO(List<TeacherDTO> teacherDTOS) {
        this.teacherDTOS = teacherDTOS;
    }

    public GroupRelationshipsDTO(ElectiveSubjectDTO electiveSubjectDTO) {
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

    public GroupRelationshipsDTO(ObligatorySubjectDTO obligatorySubjectDTO) {
        this.obligatorySubjectDTO = obligatorySubjectDTO;
    }

}
