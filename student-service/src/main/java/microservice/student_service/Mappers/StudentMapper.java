package microservice.student_service.Mappers;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Student.StudentInsertDTO;
import microservice.student_service.Model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "semestersCompleted", ignore = true)
    @Mapping(target = "incomeGeneration", ignore = true)
    Student insertDtoToEntity(StudentInsertDTO studentInsertDTO);

    StudentDTO entityToDTO(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "semestersCompleted", ignore = true)
    @Mapping(target = "incomeGeneration", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    void updatePersonalData(@MappingTarget Student student, StudentInsertDTO studentInsertDTO);
}
