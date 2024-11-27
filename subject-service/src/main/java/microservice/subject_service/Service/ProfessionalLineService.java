package microservice.subject_service.Service;

import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.common_classes.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.common_classes.Utils.Response.Result;

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
