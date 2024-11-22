package microservice.subject_service.Mappers;

import javax.annotation.processing.Generated;
import microservice.common_classes.DTOs.Area.AreaDTO;
import microservice.common_classes.DTOs.Area.AreaInsertDTO;
import microservice.common_classes.DTOs.Area.AreaWithRelationsDTO;
import microservice.subject_service.Model.Area;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T21:23:26-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.9.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class AreaMapperImpl implements AreaMapper {

    @Override
    public Area insertDtoToEntity(AreaInsertDTO studentInsertDTO) {
        if ( studentInsertDTO == null ) {
            return null;
        }

        Area area = new Area();

        area.setName( studentInsertDTO.getName() );

        area.setCreatedAt( java.time.LocalDateTime.now() );
        area.setUpdatedAt( java.time.LocalDateTime.now() );

        return area;
    }

    @Override
    public AreaDTO entityToDTO(Area area) {
        if ( area == null ) {
            return null;
        }

        AreaDTO areaDTO = new AreaDTO();

        areaDTO.setId( area.getId() );
        areaDTO.setName( area.getName() );
        areaDTO.setCreatedAt( area.getCreatedAt() );
        areaDTO.setUpdatedAt( area.getUpdatedAt() );

        return areaDTO;
    }

    @Override
    public AreaWithRelationsDTO entityToDTOWithRelations(Area area) {
        if ( area == null ) {
            return null;
        }

        AreaWithRelationsDTO areaWithRelationsDTO = new AreaWithRelationsDTO();

        areaWithRelationsDTO.setId( area.getId() );
        areaWithRelationsDTO.setName( area.getName() );
        areaWithRelationsDTO.setCreatedAt( area.getCreatedAt() );
        areaWithRelationsDTO.setUpdatedAt( area.getUpdatedAt() );

        return areaWithRelationsDTO;
    }
}
