package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.enrollment_service.Model.Preload.Grade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    Grade dtoToEntity(GradeDTO gradeDTO);
}
