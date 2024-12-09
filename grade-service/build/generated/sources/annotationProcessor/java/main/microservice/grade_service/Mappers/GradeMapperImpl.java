package microservice.grade_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.Group.GroupType;
import microservice.common_classes.Utils.SubjectType;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import microservice.grade_service.Utils.GradeTrack;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-08T17:59:58-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.9.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class GradeMapperImpl implements GradeMapper {

    @Override
    public Grade insertDtoToEntity(GradeInsertDTO gradeInsertDTO) {
        if ( gradeInsertDTO == null ) {
            return null;
        }

        Grade.GradeBuilder grade = Grade.builder();

        grade.gradeValue( gradeInsertDTO.getGradeValue() );
        grade.studentAccountNumber( gradeInsertDTO.getStudentAccountNumber() );
        if ( gradeInsertDTO.getGradeStatus() != null ) {
            grade.gradeStatus( Enum.valueOf( GradeStatus.class, gradeInsertDTO.getGradeStatus() ) );
        }

        return grade.build();
    }

    @Override
    public GradeDTO entityToDTO(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeDTO gradeDTO = new GradeDTO();

        gradeDTO.setStudentAccountNumber( grade.getStudentAccountNumber() );
        gradeDTO.setGradeValue( grade.getGradeValue() );
        gradeDTO.setGradeResult( grade.getGradeResult() );
        gradeDTO.setGradeStatus( grade.getGradeStatus() );

        return gradeDTO;
    }

    @Override
    public GradeTrack entityToNamedDTO(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeTrack gradeTrack = new GradeTrack();

        gradeTrack.setSubjectName( gradeSubjectSubjectName( grade ) );
        gradeTrack.setSubjectId( gradeSubjectId( grade ) );
        Integer subjectCredits = gradeSubjectSubjectCredits( grade );
        if ( subjectCredits != null ) {
            gradeTrack.setSubjectCredits( String.valueOf( subjectCredits ) );
        }
        gradeTrack.setSubjectType( gradeSubjectSubjectType( grade ) );
        gradeTrack.setGroupType( gradeGroupGroupType( grade ) );
        gradeTrack.setLastSchoolPeriodCoursed( grade.getSchoolPeriod() );
        if ( grade.getGradeValue() != null ) {
            gradeTrack.setGradeValue( grade.getGradeValue() );
        }

        return gradeTrack;
    }

    @Override
    public Grade enrollmentDTOtoEntity(EnrollmentDTO enrollmentDTO) {
        if ( enrollmentDTO == null ) {
            return null;
        }

        Grade.GradeBuilder grade = Grade.builder();

        grade.id( enrollmentDTO.getId() );
        grade.studentAccountNumber( enrollmentDTO.getStudentAccountNumber() );
        grade.schoolPeriod( enrollmentDTO.getSchoolPeriod() );

        return grade.build();
    }

    private String gradeSubjectSubjectName(Grade grade) {
        if ( grade == null ) {
            return null;
        }
        Subject subject = grade.getSubject();
        if ( subject == null ) {
            return null;
        }
        String subjectName = subject.getSubjectName();
        if ( subjectName == null ) {
            return null;
        }
        return subjectName;
    }

    private Long gradeSubjectId(Grade grade) {
        if ( grade == null ) {
            return null;
        }
        Subject subject = grade.getSubject();
        if ( subject == null ) {
            return null;
        }
        Long id = subject.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Integer gradeSubjectSubjectCredits(Grade grade) {
        if ( grade == null ) {
            return null;
        }
        Subject subject = grade.getSubject();
        if ( subject == null ) {
            return null;
        }
        int subjectCredits = subject.getSubjectCredits();
        return subjectCredits;
    }

    private SubjectType gradeSubjectSubjectType(Grade grade) {
        if ( grade == null ) {
            return null;
        }
        Subject subject = grade.getSubject();
        if ( subject == null ) {
            return null;
        }
        SubjectType subjectType = subject.getSubjectType();
        if ( subjectType == null ) {
            return null;
        }
        return subjectType;
    }

    private GroupType gradeGroupGroupType(Grade grade) {
        if ( grade == null ) {
            return null;
        }
        Group group = grade.getGroup();
        if ( group == null ) {
            return null;
        }
        GroupType groupType = group.getGroupType();
        if ( groupType == null ) {
            return null;
        }
        return groupType;
    }
}
