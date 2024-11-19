package microservice.common_classes.FacadeService.Subject;

import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface SubjectFacadeService {
    CompletableFuture<Boolean> validateExistingOrdinarySubject(Long subjectId);
    CompletableFuture<ObligatorySubjectDTO> getOrdinarySubjectById(Long subjectId);

    CompletableFuture<Boolean> validateExistingElectiveSubject(Long subjectId);
    CompletableFuture<ElectiveSubjectDTO> getElectiveSubjectById(Long subjectId);

    CompletableFuture<List<ObligatorySubjectDTO>> getObligatorySubjectsByCareer(String careerKey);
    CompletableFuture<List<ElectiveSubjectDTO>> getElectiveSubjectsByCareer(Long careerId);

}
