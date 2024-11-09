package microservice.grade_service.Mappers;

import microservice.grade_service.DTOs.GradeDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    @Mapping(target = "id", ignore = true)
    Grade insertDtoToEntity(GradeInsertDTO gradeInsertDTO);

    GradeDTO entityToDTO(Grade grade);

}
