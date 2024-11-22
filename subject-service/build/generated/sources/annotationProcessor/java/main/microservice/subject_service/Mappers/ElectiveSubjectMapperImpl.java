package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Model.Area;
import microservice.subject_service.Model.ElectiveSubject;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T21:23:26-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.9.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class ElectiveSubjectMapperImpl implements ElectiveSubjectMapper {

    @Override
    public ElectiveSubject insertDtoToEntity(ElectiveSubjectInsertDTO electiveSubjectInsertDTO) {
        if ( electiveSubjectInsertDTO == null ) {
            return null;
        }

        ElectiveSubject electiveSubject = new ElectiveSubject();

        electiveSubject.setName( electiveSubjectInsertDTO.getName() );

        electiveSubject.setCreatedAt( java.time.LocalDateTime.now() );
        electiveSubject.setUpdatedAt( java.time.LocalDateTime.now() );

        return electiveSubject;
    }

    @Override
    public ElectiveSubject updateDtoToEntity(ElectiveSubjectInsertDTO electiveSubjectInsertDTO, Long electiveSubjectId) {
        if ( electiveSubjectInsertDTO == null && electiveSubjectId == null ) {
            return null;
        }

        ElectiveSubject electiveSubject = new ElectiveSubject();

        if ( electiveSubjectInsertDTO != null ) {
            electiveSubject.setId( electiveSubjectInsertDTO.getId() );
            electiveSubject.setName( electiveSubjectInsertDTO.getName() );
        }
        electiveSubject.setUpdatedAt( java.time.LocalDateTime.now() );

        return electiveSubject;
    }

    @Override
    public ElectiveSubjectDTO entityToDTO(ElectiveSubject electiveSubject) {
        if ( electiveSubject == null ) {
            return null;
        }

        ElectiveSubjectDTO electiveSubjectDTO = new ElectiveSubjectDTO();

        electiveSubjectDTO.setAreaId( electiveSubjectAreaId( electiveSubject ) );
        electiveSubjectDTO.setId( electiveSubject.getId() );
        electiveSubjectDTO.setName( electiveSubject.getName() );

        return electiveSubjectDTO;
    }

    private Long electiveSubjectAreaId(ElectiveSubject electiveSubject) {
        if ( electiveSubject == null ) {
            return null;
        }
        Area area = electiveSubject.getArea();
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
