package microservice.subject_service.Mappers;

import microservice.subject_service.Model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "currentCredits", ignore = true)
    @Mapping(target = "semestersCompleted", ignore = true)
    @Mapping(target = "incomeGeneration", ignore = true)
    Subject insertDtoToEntity(StudentInsertDTO studentInsertDTO);

    StudentDTO entityToDTO(Subject subject);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "semestersCompleted", ignore = true)
    @Mapping(target = "incomeGeneration", ignore = true)
    @Mapping(target = "currentCredits", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Subject updatePersonalData(@MappingTarget Subject subject, StudentInsertDTO studentInsertDTO);
}
