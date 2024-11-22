package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.subject_service.Model.ProfessionalLine;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-19T14:21:09-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class ProfessionalLineMapperImpl implements ProfessionalLineMapper {

    @Override
    public ProfessionalLine insertDtoToEntity(ProfessionalLineInsertDTO professionalLineInsertDTO) {
        if ( professionalLineInsertDTO == null ) {
            return null;
        }

        ProfessionalLine professionalLine = new ProfessionalLine();

        professionalLine.setName( professionalLineInsertDTO.getName() );

        professionalLine.setCreatedAt( java.time.LocalDateTime.now() );
        professionalLine.setUpdatedAt( java.time.LocalDateTime.now() );

        return professionalLine;
    }

    @Override
    public ProfessionalLineDTO entityToDTO(ProfessionalLine professionalLine) {
        if ( professionalLine == null ) {
            return null;
        }

        ProfessionalLineDTO professionalLineDTO = new ProfessionalLineDTO();

        professionalLineDTO.setId( professionalLine.getId() );
        professionalLineDTO.setName( professionalLine.getName() );

        return professionalLineDTO;
    }
}
