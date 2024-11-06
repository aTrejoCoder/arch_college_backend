package microservice.subject_service.Mappers;

import microservice.subject_service.DTOs.Career.CareerDTO;
import microservice.subject_service.DTOs.Career.CareerInsertDTO;
import microservice.subject_service.Model.Career;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CareerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ordinarySubjects", ignore = true)
    @Mapping(target = "electiveSubjects", ignore = true)
    Career insertDtoToEntity(CareerInsertDTO careerInsertDTO);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ordinarySubjects", ignore = true)
    @Mapping(target = "electiveSubjects", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Career career , CareerInsertDTO careerInsertDTO);

    CareerDTO entityToDTO(Career career);
}
