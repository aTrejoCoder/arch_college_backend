package microservice.academic_curriculum_service.Mappers;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Carrer.CareerInsertDTO;
import microservice.academic_curriculum_service.Model.Career.Career;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CareerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "obligatorySubjects", ignore = true)
    @Mapping(target = "electiveSubjects", ignore = true)
    Career insertDtoToEntity(CareerInsertDTO careerInsertDTO);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "obligatorySubjects", ignore = true)
    @Mapping(target = "electiveSubjects", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Career career , CareerInsertDTO careerInsertDTO);

    CareerDTO entityToDTO(Career career);
}
