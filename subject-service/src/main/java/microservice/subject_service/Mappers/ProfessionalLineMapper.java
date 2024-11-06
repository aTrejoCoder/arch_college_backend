package microservice.subject_service.Mappers;

import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.subject_service.Model.ProfessionalLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfessionalLineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ProfessionalLine insertDtoToEntity(ProfessionalLineInsertDTO professionalLineInsertDTO);

    ProfessionalLineDTO entityToDTO(ProfessionalLine professionalLine);
}
