package microservice.grade_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.GradeNamed;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-26T17:45:37-0600",
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
        grade.setOrdinarySubjectId( gradeInsertDTO.getOrdinarySubjectId() );
        grade.setElectiveSubjectId( gradeInsertDTO.getElectiveSubjectId() );
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
        gradeDTO.setGradeStatus( gradeStatusToGradeStatus( grade.getGradeStatus() ) );
        gradeDTO.setOrdinarySubjectId( grade.getOrdinarySubjectId() );
        gradeDTO.setElectiveSubjectId( grade.getElectiveSubjectId() );
        gradeDTO.setGroupId( grade.getGroupId() );

        return gradeDTO;
    }

    @Override
    public GradeNamed entityToNamedDTO(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeNamed gradeNamed = new GradeNamed();

        gradeNamed.setSubjectName( grade.getSubjectName() );
        gradeNamed.setGradeValue( grade.getGradeValue() );
        gradeNamed.setSchoolPeriod( grade.getSchoolPeriod() );
        gradeNamed.setAuthorized( grade.isAuthorized() );

        return gradeNamed;
    }

    protected GradeStatus gradeStatusToGradeStatus(microservice.grade_service.Utils.GradeStatus gradeStatus) {
        if ( gradeStatus == null ) {
            return null;
        }

        GradeStatus gradeStatus1;

        switch ( gradeStatus ) {
            case NORMAL_PENDING: gradeStatus1 = GradeStatus.NORMAL_PENDING;
            break;
            case DID_NOT_PRESENT_PENDING: gradeStatus1 = GradeStatus.DID_NOT_PRESENT_PENDING;
            break;
            case ACCREDITED_PENDING: gradeStatus1 = GradeStatus.ACCREDITED_PENDING;
            break;
            case NORMAL: gradeStatus1 = GradeStatus.NORMAL;
            break;
            case DID_NOT_PRESENT: gradeStatus1 = GradeStatus.DID_NOT_PRESENT;
            break;
            case ACCREDITED: gradeStatus1 = GradeStatus.ACCREDITED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + gradeStatus );
        }

        return gradeStatus1;
    }
}
