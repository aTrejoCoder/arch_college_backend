package microservice.academic_curriculum_service.Mappers;

import microservice.academic_curriculum_service.Model.Subject.ObligatorySubject;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectInsertDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ObligatorySubjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ObligatorySubject insertDtoToEntity(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ObligatorySubject updateDtoToEntity(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO, Long ordinarySubjectId);

    @Mapping(target = "areaId", source = "area.id")
    @Mapping(target = "seriesId", source = "series.id")
    ObligatorySubjectDTO entityToDTO(ObligatorySubject obligatorySubject);
}
