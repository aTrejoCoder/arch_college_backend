package microservice.common_classes.FacadeService.AcademicCurriculumService;

import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.common_classes.Utils.CustomPage;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface AcademicCurriculumFacadeService {
    CompletableFuture<ObligatorySubjectDTO> getOrdinarySubjectById(Long subjectId);
    CompletableFuture<CareerDTO> getCareerById(Long careerId);
    CompletableFuture<ProfessionalLineDTO> getProfessionalLineById(Long professionalLineId);
    CompletableFuture<ElectiveSubjectDTO> getElectiveSubjectById(Long subjectId);

    CompletableFuture<List<ObligatorySubjectDTO>> getObligatorySubjectsByCareer(String careerKey);
    CompletableFuture<List<ElectiveSubjectDTO>> getElectiveSubjectsByCareer(Long careerId);

    CustomPage<ObligatorySubjectDTO> getObligatorySubjectsPageable(int page, int size);
    CustomPage<ElectiveSubjectDTO> getElectiveSubjectsPageable(int page, int size);
    CustomPage<SubjectSeriesDTO> getSubjectSeriesPageable(int page, int pageSize);

}
