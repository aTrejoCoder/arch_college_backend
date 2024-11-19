package microservice.grade_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.DTOs.GradeWithRelationsDTO;
import microservice.grade_service.Model.Grade;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-18T20:40:08-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class GradeMapperImpl implements GradeMapper {

    @Override
    public Grade insertDtoToEntity(GradeInsertDTO gradeInsertDTO) {
        if ( gradeInsertDTO == null ) {
            return null;
        }

        Grade grade = new Grade();

        grade.setGradeValue( gradeInsertDTO.getGradeValue() );
        grade.setGradeStatus( gradeInsertDTO.getGradeStatus() );
        grade.setOrdinarySubjectId( gradeInsertDTO.getOrdinarySubjectId() );
        grade.setElectiveSubjectId( gradeInsertDTO.getElectiveSubjectId() );
        grade.setStudentAccountNumber( gradeInsertDTO.getStudentAccountNumber() );
        grade.setGroupId( gradeInsertDTO.getGroupId() );

        return grade;
    }

    @Override
    public GradeWithRelationsDTO entityToDTO(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeWithRelationsDTO gradeWithRelationsDTO = new GradeWithRelationsDTO();

        gradeWithRelationsDTO.setId( grade.getId() );
        gradeWithRelationsDTO.setGradeValue( grade.getGradeValue() );
        gradeWithRelationsDTO.setGradeStatus( grade.getGradeStatus() );

        return gradeWithRelationsDTO;
    }
}
