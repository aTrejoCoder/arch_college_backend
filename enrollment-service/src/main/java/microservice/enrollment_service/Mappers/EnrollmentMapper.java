package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.enrollment_service.Model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    Enrollment insertDtoToEntity(EnrollmentInsertDTO studentInsertDTO);

    EnrollmentDTO entityToDTO(Enrollment student);


}
