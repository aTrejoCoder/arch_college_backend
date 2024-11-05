package microservice.subject_service.Mappers;

import microservice.subject_service.DTOs.Area.AreaDTO;
import microservice.subject_service.DTOs.Area.AreaInsertDTO;
import microservice.subject_service.Model.Area;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AreaMapper {

    @Mapping(target = "areaId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Area insertDtoToEntity(AreaInsertDTO studentInsertDTO);

    AreaDTO entityToDTO(Area subject);
}
