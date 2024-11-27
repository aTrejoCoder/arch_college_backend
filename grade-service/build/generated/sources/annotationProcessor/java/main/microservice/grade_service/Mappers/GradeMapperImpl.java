package microservice.grade_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.GradeNamed;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-26T23:17:48-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.9.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
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
        grade.setSubjectId( gradeInsertDTO.getSubjectId() );
        grade.setSubjectType( gradeInsertDTO.getSubjectType() );
        grade.setStudentAccountNumber( gradeInsertDTO.getStudentAccountNumber() );
        grade.setGroupId( gradeInsertDTO.getGroupId() );

        return grade;
    }

    @Override
    public GradeDTO entityToDTO(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeDTO gradeDTO = new GradeDTO();

        gradeDTO.setId( grade.getId() );
        gradeDTO.setGradeValue( grade.getGradeValue() );
        gradeDTO.setGradeStatus( grade.getGradeStatus() );
        gradeDTO.setGroupId( grade.getGroupId() );

        return gradeDTO;
    }

    @Override
    public GradeNamed entityToNamedDTO(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeNamed gradeNamed = new GradeNamed();

        gradeNamed.setSubjectType( grade.getSubjectType() );
        gradeNamed.setSubjectName( grade.getSubjectName() );
        gradeNamed.setGradeValue( grade.getGradeValue() );
        gradeNamed.setSchoolPeriod( grade.getSchoolPeriod() );
        gradeNamed.setAuthorized( grade.isAuthorized() );

        return gradeNamed;
    }
}
