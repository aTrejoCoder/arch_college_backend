package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdinarySubjectService {
    Result<OrdinarySubjectDTO> getById(Long areaId);
    Result<OrdinarySubjectDTO> getByName(String name);
    Page<OrdinarySubjectDTO> getBySemester(int semester, Pageable pageable);
    Page<OrdinarySubjectDTO> getsById(Long areaId, Pageable pageable);
    Page<OrdinarySubjectDTO> getAllSubjects(Pageable pageable);

    void createOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO);
    void updateOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO, Long subjectId);
    void deleteOrdinarySubject(Long subjectId);
}
