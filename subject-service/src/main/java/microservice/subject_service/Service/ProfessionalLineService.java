package microservice.subject_service.Service;

import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;

import java.util.List;

public interface ProfessionalLineService {
    Result<ProfessionalLineDTO> getProfessionalLineById(Long professionalLineId);
    Result<ProfessionalLineDTO> getProfessionalLineByIdWithSubjects(Long professionalLineId);
    Result<ProfessionalLineDTO> getProfessionalLineByName(String name);
    List<ProfessionalLineDTO> getAllProfessionalLines();
    void createProfessionalLine(ProfessionalLineInsertDTO professionalLineInsertDTO);
    void updateProfessionalLineName(ProfessionalLineInsertDTO professionalLineInsertDTO, Long professionalLineId);
    void deleteProfessionalLine(Long professionalLineId);
}
