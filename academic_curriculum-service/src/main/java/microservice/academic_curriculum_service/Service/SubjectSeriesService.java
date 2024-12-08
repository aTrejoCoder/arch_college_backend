package microservice.academic_curriculum_service.Service;

import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.academic_curriculum_service.DTOs.SocialNetwork.SubjectSeriesInsertDTO;
import microservice.common_classes.Utils.Response.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubjectSeriesService {
    Result<Void> validateSubjectSeriesCreation(SubjectSeriesInsertDTO subjectSeriesInsertDTO);
    SubjectSeriesDTO createSubjectSeries(SubjectSeriesInsertDTO subjectSeriesInsertDTO);
    SubjectSeriesDTO getSubjectSeriesByObligatorySubjectId(Long obligatorySubjectId);
    SubjectSeriesDTO getSubjectSeriesByElectiveSubjectId(Long electiveSubjectId);
    Page<SubjectSeriesDTO> getAll(Pageable pageable);
    void deleteSubjectSeriesById(Long subjectSeriesId);
}
