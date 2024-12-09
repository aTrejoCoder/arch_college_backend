package microservice.grade_service.Mappers;

import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Utils.GradeTrack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    @Mapping(target = "id", ignore = true)
    Grade insertDtoToEntity(GradeInsertDTO gradeInsertDTO);

    GradeDTO entityToDTO(Grade grade);

    @Mapping(target = "subjectName", source = "subject.subjectName")
    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "subjectCredits", source = "subject.subjectCredits")
    @Mapping(target = "subjectType", source = "subject.subjectType")
    @Mapping(target = "groupType", source = "group.groupType")
    @Mapping(target = "lastSchoolPeriodCoursed", source = "schoolPeriod")
    GradeTrack entityToNamedDTO(Grade grade);

    @Mapping(target = "gradeValue", ignore = true)
    @Mapping(target = "authorizedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Grade enrollmentDTOtoEntity(EnrollmentDTO enrollmentDTO);
}
