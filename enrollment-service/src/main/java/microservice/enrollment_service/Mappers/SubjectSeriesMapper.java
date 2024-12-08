package microservice.enrollment_service.Mappers;

import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.enrollment_service.Model.Preload.Student;
import microservice.enrollment_service.Model.Preload.SubjectSeries;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectSeriesMapper {

    SubjectSeries dtoToEntity(SubjectSeriesDTO subjectSeriesDTO);
}
