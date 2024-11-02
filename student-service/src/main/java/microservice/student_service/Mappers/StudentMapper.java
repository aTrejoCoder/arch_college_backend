package microservice.student_service.Mappers;

import microservice.student_service.DTOs.StudentDTO;
import microservice.student_service.DTOs.StudentInsertDTO;
import microservice.student_service.Model.Student;
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
    Student insertDtoToEntity(StudentInsertDTO studentInsertDTO);

    StudentDTO entityToDTO(Student student);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "semestersCompleted", ignore = true)
    @Mapping(target = "incomeGeneration", ignore = true)
    @Mapping(target = "currentCredits", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Student updatePersonalData(@MappingTarget Student student, StudentInsertDTO studentInsertDTO);
}
