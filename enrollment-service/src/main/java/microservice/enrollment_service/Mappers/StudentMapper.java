package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.enrollment_service.Model.Preload.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student dtoToEntity(StudentDTO groupDTO);
}
