package microservice.academic_curriculum_service.Mappers;

import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.academic_curriculum_service.DTOs.SocialNetwork.SubjectSeriesInsertDTO;
import microservice.academic_curriculum_service.Model.Subject.SubjectSeries;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ObligatorySubjectMapper.class, ElectiveSubjectMapper.class})
public interface SubjectSeriesMapper {

    SubjectSeries insertDtoToEntity(SubjectSeriesInsertDTO insetDTO);

    @Mapping(target = "obligatorySubjects", source = "obligatorySubjects")
    @Mapping(target = "electiveSubjects", source = "electiveSubjects")
    SubjectSeriesDTO entityToDTO(SubjectSeries subjectSeries);

}
