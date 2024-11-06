package microservice.subject_service.Mappers;

import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import microservice.subject_service.Model.OrdinarySubject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrdinarySubjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    OrdinarySubject insertDtoToEntity(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    OrdinarySubject updateDtoToEntity(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO, Long ordinarySubjectId);

    OrdinarySubjectDTO entityToDTO(OrdinarySubject ordinarySubject);
}
