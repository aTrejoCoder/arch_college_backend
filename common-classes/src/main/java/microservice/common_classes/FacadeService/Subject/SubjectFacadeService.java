package microservice.common_classes.FacadeService.Subject;

import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface SubjectFacadeService {
    CompletableFuture<Boolean> validateExistingOrdinarySubject(Long subjectId);
    CompletableFuture<OrdinarySubjectDTO> getOrdinarySubjectById(Long subjectId);

    CompletableFuture<Boolean> validateExistingElectiveSubject(Long subjectId);
    CompletableFuture<ElectiveSubjectDTO> getElectiveSubjectById(Long subjectId);

    CompletableFuture<List<OrdinarySubjectDTO>> getObligatorySubjectsByCareer(String careerKey);
    CompletableFuture<List<ElectiveSubjectDTO>> getElectiveSubjectsByCareer(String careerKey);

}
