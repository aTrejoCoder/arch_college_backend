package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElectiveSubjectService {
    Result<ElectiveSubjectDTO> getSubjectById(Long subjectId);
    Result<ElectiveSubjectDTO> getSubjectByName(String name);
    Page<ElectiveSubjectDTO> getSubjectByProfessionalLineId(Long professionalLineId, Pageable pageable);
    Page<ElectiveSubjectDTO> getSubjectByAreaId(Long subjectId, Pageable pageable);
    Page<ElectiveSubjectDTO> getSubjectAll(Pageable pageable);

    void createElectiveSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO);
    void updateElectiveSubject(ElectiveSubjectInsertDTO electiveSubjectInsertDTO, Long subjectId);
    void deleteElectiveSubject(Long subjectId);
}
