package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Model.ElectiveSubject;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-05T17:45:23-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
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
        electiveSubject.setSubjectProgramUrl( electiveSubjectInsertDTO.getSubjectProgramUrl() );

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
            electiveSubject.setName( electiveSubjectInsertDTO.getName() );
            electiveSubject.setSubjectProgramUrl( electiveSubjectInsertDTO.getSubjectProgramUrl() );
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

        electiveSubjectDTO.setId( electiveSubject.getId() );
        electiveSubjectDTO.setName( electiveSubject.getName() );

        return electiveSubjectDTO;
    }
}
