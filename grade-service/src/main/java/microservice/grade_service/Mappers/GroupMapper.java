package microservice.grade_service.Mappers;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.grade_service.DTOs.GroupDTO;
import microservice.grade_service.Model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GradeMapper.class)
public interface GroupMapper {

    Group enrollmentDTOtoEntity(EnrollmentDTO enrollmentDTO);

    @Mapping(target = "grades", source = "grades")
    @Mapping(target = "subjectName", source = "group.subject.subjectName")
    GroupDTO entityToDTO(Group group);

}
