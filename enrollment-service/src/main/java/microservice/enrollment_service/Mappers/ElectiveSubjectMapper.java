package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ElectiveSubjectMapper {

    ElectiveSubject dtoToEntity(ElectiveSubjectDTO electiveSubjectDTO);
}
