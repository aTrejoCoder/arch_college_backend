package microservice.subject_service.Mappers;

import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Model.ElectiveSubject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ElectiveSubjectMapper {

    @Mapping(target = "electiveSubjectId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ElectiveSubject insertDtoToEntity(ElectiveSubjectInsertDTO electiveSubjectInsertDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    ElectiveSubject updateDtoToEntity(ElectiveSubjectInsertDTO electiveSubjectInsertDTO, Long electiveSubjectId);

    ElectiveSubjectDTO entityToDTO(ElectiveSubject electiveSubject);
}
