package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdinarySubjectService {
    Result<OrdinarySubjectDTO> getSubjectById(Long areaId);
    Result<OrdinarySubjectDTO> getSubjectByName(String name);
    Page<OrdinarySubjectDTO> getSubjectBySemester(int semester, Pageable pageable);
    Page<OrdinarySubjectDTO> getAllSubjects(Pageable pageable);
    Page<OrdinarySubjectDTO> getSubjectByAreaId(Long areaId, Pageable pageable);

    void createOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO);
    void updateOrdinarySubject(OrdinarySubjectInsertDTO ordinarySubjectInsertDTO, Long subjectId);
    void deleteOrdinarySubject(Long subjectId);
}
