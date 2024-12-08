package microservice.schedule_service.Mapppers;

import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.schedule_service.Models.Teacher;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    Teacher dtoToEntity(TeacherDTO teacherDTO);
    List<Teacher> teacherDTOsToEntities(List<TeacherDTO> teacherDTOs);
    List<TeacherNameDTO> teachersToDTOs(List<Teacher> teachers);

}
