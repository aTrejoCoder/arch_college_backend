package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObligatorySubjectMapper {

    ObligatorySubject dtoToEntity(ObligatorySubjectDTO obligatorySubjectDTO);
}
