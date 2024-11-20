package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectInsertDTO;
import microservice.subject_service.Model.Area;
import microservice.subject_service.Model.ObligatorySubject;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-19T14:21:09-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class ObligatorySubjectMapperImpl implements ObligatorySubjectMapper {

    @Override
    public ObligatorySubject insertDtoToEntity(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO) {
        if ( obligatorySubjectInsertDTO == null ) {
            return null;
        }

        ObligatorySubject obligatorySubject = new ObligatorySubject();

        obligatorySubject.setName( obligatorySubjectInsertDTO.getName() );
        obligatorySubject.setNumber( obligatorySubjectInsertDTO.getNumber() );
        obligatorySubject.setSemester( obligatorySubjectInsertDTO.getSemester() );
        obligatorySubject.setCredits( obligatorySubjectInsertDTO.getCredits() );

        obligatorySubject.setCreatedAt( java.time.LocalDateTime.now() );
        obligatorySubject.setUpdatedAt( java.time.LocalDateTime.now() );

        return obligatorySubject;
    }

    @Override
    public ObligatorySubject updateDtoToEntity(ObligatorySubjectInsertDTO obligatorySubjectInsertDTO, Long ordinarySubjectId) {
        if ( obligatorySubjectInsertDTO == null && ordinarySubjectId == null ) {
            return null;
        }

        ObligatorySubject obligatorySubject = new ObligatorySubject();

        if ( obligatorySubjectInsertDTO != null ) {
            obligatorySubject.setId( obligatorySubjectInsertDTO.getId() );
            obligatorySubject.setName( obligatorySubjectInsertDTO.getName() );
            obligatorySubject.setNumber( obligatorySubjectInsertDTO.getNumber() );
            obligatorySubject.setSemester( obligatorySubjectInsertDTO.getSemester() );
            obligatorySubject.setCredits( obligatorySubjectInsertDTO.getCredits() );
        }
        obligatorySubject.setUpdatedAt( java.time.LocalDateTime.now() );

        return obligatorySubject;
    }

    @Override
    public ObligatorySubjectDTO entityToDTO(ObligatorySubject obligatorySubject) {
        if ( obligatorySubject == null ) {
            return null;
        }

        ObligatorySubjectDTO obligatorySubjectDTO = new ObligatorySubjectDTO();

        obligatorySubjectDTO.setAreaId( obligatorySubjectAreaId( obligatorySubject ) );
        obligatorySubjectDTO.setId( obligatorySubject.getId() );
        obligatorySubjectDTO.setName( obligatorySubject.getName() );
        obligatorySubjectDTO.setNumber( obligatorySubject.getNumber() );
        obligatorySubjectDTO.setSemester( obligatorySubject.getSemester() );
        obligatorySubjectDTO.setCredits( obligatorySubject.getCredits() );

        return obligatorySubjectDTO;
    }

    private Long obligatorySubjectAreaId(ObligatorySubject obligatorySubject) {
        if ( obligatorySubject == null ) {
            return null;
        }
        Area area = obligatorySubject.getArea();
        if ( area == null ) {
            return null;
        }
        Long id = area.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
