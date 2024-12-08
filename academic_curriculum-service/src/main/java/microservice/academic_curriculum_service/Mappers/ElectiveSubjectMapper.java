package microservice.academic_curriculum_service.Mappers;

import microservice.academic_curriculum_service.Model.Subject.ElectiveSubject;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ElectiveSubjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ElectiveSubject insertDtoToEntity(ElectiveSubjectInsertDTO electiveSubjectInsertDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ElectiveSubject updateDtoToEntity(ElectiveSubjectInsertDTO electiveSubjectInsertDTO, Long electiveSubjectId);

    @Mapping(target = "areaId", source = "area.id")
    @Mapping(target = "seriesId", source = "series.id")
    @Mapping(target = "professionalLineId", source = "professionalLine.id")
    ElectiveSubjectDTO entityToDTO(ElectiveSubject electiveSubject);
}
