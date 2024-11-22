package microservice.grade_service.Service;


import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.DTOs.GradeRelationshipsDTO;

public interface GradeRelationshipService {
    Result<GradeRelationshipsDTO> validateGradeRelationship(GradeInsertDTO gradeInsertDTO);
}
