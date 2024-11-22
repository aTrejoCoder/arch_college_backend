package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Carrer.CareerInsertDTO;
import microservice.subject_service.Model.Career;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-19T14:21:09-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class CareerMapperImpl implements CareerMapper {

    @Override
    public Career insertDtoToEntity(CareerInsertDTO careerInsertDTO) {
        if ( careerInsertDTO == null ) {
            return null;
        }

        Career career = new Career();

        career.setName( careerInsertDTO.getName() );
        career.setTitleAwarded( careerInsertDTO.getTitleAwarded() );
        career.setModality( careerInsertDTO.getModality() );
        career.setSemesterDuration( careerInsertDTO.getSemesterDuration() );
        career.setTotalCareerCredits( careerInsertDTO.getTotalCareerCredits() );
        career.setTotalObligatoryCredits( careerInsertDTO.getTotalObligatoryCredits() );
        career.setTotalElectiveCredits( careerInsertDTO.getTotalElectiveCredits() );
        career.setCareerDirectorId( careerInsertDTO.getCareerDirectorId() );

        career.setCreatedAt( java.time.LocalDateTime.now() );
        career.setUpdatedAt( java.time.LocalDateTime.now() );

        return career;
    }

    @Override
    public void updateEntity(Career career, CareerInsertDTO careerInsertDTO) {
        if ( careerInsertDTO == null ) {
            return;
        }

        career.setName( careerInsertDTO.getName() );
        career.setTitleAwarded( careerInsertDTO.getTitleAwarded() );
        career.setModality( careerInsertDTO.getModality() );
        career.setSemesterDuration( careerInsertDTO.getSemesterDuration() );
        career.setTotalCareerCredits( careerInsertDTO.getTotalCareerCredits() );
        career.setTotalObligatoryCredits( careerInsertDTO.getTotalObligatoryCredits() );
        career.setTotalElectiveCredits( careerInsertDTO.getTotalElectiveCredits() );
        career.setCareerDirectorId( careerInsertDTO.getCareerDirectorId() );

        career.setUpdatedAt( java.time.LocalDateTime.now() );
    }

    @Override
    public CareerDTO entityToDTO(Career career) {
        if ( career == null ) {
            return null;
        }

        CareerDTO careerDTO = new CareerDTO();

        careerDTO.setId( career.getId() );
        careerDTO.setKey( career.getKey() );
        careerDTO.setName( career.getName() );
        careerDTO.setTitleAwarded( career.getTitleAwarded() );
        careerDTO.setModality( career.getModality() );
        careerDTO.setSemesterDuration( career.getSemesterDuration() );
        careerDTO.setTotalCareerCredits( career.getTotalCareerCredits() );
        careerDTO.setTotalObligatoryCredits( career.getTotalObligatoryCredits() );
        careerDTO.setTotalElectiveCredits( career.getTotalElectiveCredits() );
        careerDTO.setCareerDirectorId( career.getCareerDirectorId() );

        return careerDTO;
    }
}
