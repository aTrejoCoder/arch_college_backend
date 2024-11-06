package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import microservice.subject_service.Model.OrdinarySubject;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-05T17:45:23-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class OrdinarySubjectMapperImpl implements OrdinarySubjectMapper {

    @Override
    public OrdinarySubject insertDtoToEntity(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO) {
        if ( ordinarySubjectInsertDTO == null ) {
            return null;
        }

        OrdinarySubject ordinarySubject = new OrdinarySubject();

        ordinarySubject.setName( ordinarySubjectInsertDTO.getName() );
        ordinarySubject.setNumber( ordinarySubjectInsertDTO.getNumber() );
        ordinarySubject.setSemester( ordinarySubjectInsertDTO.getSemester() );
        ordinarySubject.setCredits( ordinarySubjectInsertDTO.getCredits() );

        ordinarySubject.setCreatedAt( java.time.LocalDateTime.now() );
        ordinarySubject.setUpdatedAt( java.time.LocalDateTime.now() );

        return ordinarySubject;
    }

    @Override
    public OrdinarySubject updateDtoToEntity(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO, Long ordinarySubjectId) {
        if ( ordinarySubjectInsertDTO == null && ordinarySubjectId == null ) {
            return null;
        }

        OrdinarySubject ordinarySubject = new OrdinarySubject();

        if ( ordinarySubjectInsertDTO != null ) {
            ordinarySubject.setName( ordinarySubjectInsertDTO.getName() );
            ordinarySubject.setNumber( ordinarySubjectInsertDTO.getNumber() );
            ordinarySubject.setSemester( ordinarySubjectInsertDTO.getSemester() );
            ordinarySubject.setCredits( ordinarySubjectInsertDTO.getCredits() );
        }
        ordinarySubject.setUpdatedAt( java.time.LocalDateTime.now() );

        return ordinarySubject;
    }

    @Override
    public OrdinarySubjectDTO entityToDTO(OrdinarySubject ordinarySubject) {
        if ( ordinarySubject == null ) {
            return null;
        }

        OrdinarySubjectDTO ordinarySubjectDTO = new OrdinarySubjectDTO();

        ordinarySubjectDTO.setId( ordinarySubject.getId() );
        ordinarySubjectDTO.setName( ordinarySubject.getName() );
        ordinarySubjectDTO.setNumber( ordinarySubject.getNumber() );
        ordinarySubjectDTO.setCredits( ordinarySubject.getCredits() );

        return ordinarySubjectDTO;
    }
}
