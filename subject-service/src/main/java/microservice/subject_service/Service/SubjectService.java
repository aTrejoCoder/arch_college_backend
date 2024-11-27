package microservice.subject_service.Service;

import microservice.common_classes.DTOs.Subject.SubjectDTO;
import microservice.common_classes.Utils.Response.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService<T extends SubjectDTO, I extends SubjectDTO> {
    Result<T> getSubjectById(Long id);
    Result<T> getSubjectByName(String name);
    Page<T> getSubjectsByFilterPageable(Long filterId, String filterType, Pageable pageable);
    Page<T> getAllSubjectsPageable(Pageable pageable);
    List<T> getSubjectsByFilter(Long filterId, String filterType);


    void createSubject(I subjectInsertDTO);
    void updateSubject(I subjectInsertDTO, Long subjectId);
    void deleteSubject(Long subjectId);
}