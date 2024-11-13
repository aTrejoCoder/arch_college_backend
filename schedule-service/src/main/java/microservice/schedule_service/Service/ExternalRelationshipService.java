package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.GroupRelationshipsDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.schedule_service.Models.Group;
import org.springframework.stereotype.Service;

@Service
public class ExternalRelationshipService {

    public void addRelationshipSubject(Group group, GroupRelationshipsDTO relationshipsDTO) {
        if (relationshipsDTO.getElectiveSubjectDTO() != null) {
            ElectiveSubjectDTO electiveSubject = relationshipsDTO.getElectiveSubjectDTO();
            group.setSubjectName(electiveSubject.getName());
            group.setElectiveSubjectId(electiveSubject.getId());
        } else if (relationshipsDTO.getOrdinarySubjectDTO() != null) {
            OrdinarySubjectDTO ordinarySubject = relationshipsDTO.getOrdinarySubjectDTO();
            group.setSubjectName(ordinarySubject.getName() + " " + ordinarySubject.getNumber());
            group.setOrdinarySubjectId(ordinarySubject.getId());
        }
    }

    public void addTeacher(Group group, GroupRelationshipsDTO groupRelationshipsDTO) {
        TeacherDTO teacherDTO = groupRelationshipsDTO.getTeacherDTO();
        if (teacherDTO != null) {
            group.setTeacherId(teacherDTO.getTeacherId());
            group.setTeacherName(teacherDTO.getTitle().getInitials() + " " + teacherDTO.getFirstName() + " " + teacherDTO.getLastName());
        }
    }
}