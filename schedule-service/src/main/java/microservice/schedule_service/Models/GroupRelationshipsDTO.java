package microservice.schedule_service.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;


import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class GroupRelationshipsDTO {

    private List<TeacherDTO> teacherDTOS;
    private OrdinarySubjectDTO ordinarySubjectDTO;
    private ElectiveSubjectDTO electiveSubjectDTO;

    public GroupRelationshipsDTO(ElectiveSubjectDTO electiveSubjectDTO, TeacherDTO teacherDTO) {
        this.teacherDTOS = Collections.singletonList(teacherDTO);
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

        public GroupRelationshipsDTO(OrdinarySubjectDTO ordinarySubjectDTO, List<TeacherDTO> teacherDTOS) {
        this.ordinarySubjectDTO = ordinarySubjectDTO;
        this.teacherDTOS = teacherDTOS;
    }

    public GroupRelationshipsDTO(List<TeacherDTO> teacherDTOS) {
        this.teacherDTOS = teacherDTOS;
    }

    public GroupRelationshipsDTO(ElectiveSubjectDTO electiveSubjectDTO) {
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

    public GroupRelationshipsDTO(OrdinarySubjectDTO ordinarySubjectDTO) {
        this.ordinarySubjectDTO = ordinarySubjectDTO;
    }


    public List<TeacherDTO> getTeacherDTOS() {
        return teacherDTOS;
    }

    public void setTeacherDTOS(List<TeacherDTO> teacherDTOS) {
        this.teacherDTOS = teacherDTOS;
    }

    public ElectiveSubjectDTO getElectiveSubjectDTO() {
        return electiveSubjectDTO;
    }

    public void setElectiveSubjectDTO(ElectiveSubjectDTO electiveSubjectDTO) {
        this.electiveSubjectDTO = electiveSubjectDTO;
    }

    public OrdinarySubjectDTO getOrdinarySubjectDTO() {
        return ordinarySubjectDTO;
    }

    public void setOrdinarySubjectDTO(OrdinarySubjectDTO ordinarySubjectDTO) {
        this.ordinarySubjectDTO = ordinarySubjectDTO;
    }
}
