package microservice.grade_service.Mappers;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    Subject enrollmentDTOtoEntity(EnrollmentDTO enrollmentDTO);
}
