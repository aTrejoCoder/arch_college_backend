package microservice.enrollment_service.Mappers;

import microservice.enrollment_service.DTOs.EnrollmentDTO;
import microservice.enrollment_service.DTOs.EnrollmentInsertDTO;
import microservice.enrollment_service.Model.GroupEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    GroupEnrollment insertDtoToEntity(EnrollmentInsertDTO studentInsertDTO);

    EnrollmentDTO entityToDTO(GroupEnrollment student);


}
