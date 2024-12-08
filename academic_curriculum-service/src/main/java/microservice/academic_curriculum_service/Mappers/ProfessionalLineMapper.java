package microservice.academic_curriculum_service.Mappers;


import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.academic_curriculum_service.Model.Career.ProfessionalLine;
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
