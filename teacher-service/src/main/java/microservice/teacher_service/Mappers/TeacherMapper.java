package microservice.teacher_service.Mappers;

import microservice.teacher_service.DTOs.TeacherDTO;
import microservice.teacher_service.DTOs.TeacherInsertDTO;
import microservice.teacher_service.Model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    Teacher insertDtoToEntity(TeacherInsertDTO studentInsertDTO);

    TeacherDTO entityToDTO(Teacher teacher);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updatePersonalData(@MappingTarget Teacher teacher, TeacherInsertDTO studentInsertDTO);
}
