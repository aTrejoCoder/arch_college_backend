package microservice.schedule_service.Service;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.GroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupRelationshipsDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.FacadeService.Teacher.TeacherFacadeService;
import microservice.common_classes.Utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ExternalDataValidationService {

    private final SubjectFacadeService subjectFacadeService;
    private final TeacherFacadeService teacherFacadeService;

    @Autowired
    public ExternalDataValidationService(@Qualifier("subjectFacadeServiceImpl")  SubjectFacadeService subjectFacadeService,
                                         @Qualifier("teacherFacadeServiceImpl") TeacherFacadeService teacherFacadeService) {
        this.subjectFacadeService = subjectFacadeService;
        this.teacherFacadeService = teacherFacadeService;
    }

    public Result<GroupRelationshipsDTO> validateAndGetRelationships(GroupInsertDTO groupInsertDTO) {
        if (groupInsertDTO.getOrdinarySubjectId() != null && groupInsertDTO.getElectiveSubjectId() == null) {
            return validateOrdinarySubjectRelationship(groupInsertDTO);

        } else if (groupInsertDTO.getOrdinarySubjectId() == null && groupInsertDTO.getElectiveSubjectId() != null) {
            return validateElectiveSubjectRelationship(groupInsertDTO);
        }
        return Result.error("No Subject Id provided");
    }

    private Result<GroupRelationshipsDTO> validateOrdinarySubjectRelationship(GroupInsertDTO groupInsertDTO) {
        if (groupInsertDTO.getOrdinarySubjectId() == null) {
            return Result.error("Ordinary subject ID cannot be null.");
        }
        if (groupInsertDTO.getTeacherId() == null) {
            return Result.error("Teacher ID cannot be null.");
        }


        CompletableFuture<OrdinarySubjectDTO> ordinarySubjectFuture = subjectFacadeService.getOrdinarySubjectById(groupInsertDTO.getOrdinarySubjectId());
        CompletableFuture<TeacherDTO> teacherFuture = teacherFacadeService.getTeacherById(groupInsertDTO.getTeacherId());

        return CompletableFuture.allOf(ordinarySubjectFuture, teacherFuture).thenApply(v -> {
            OrdinarySubjectDTO ordinarySubject = ordinarySubjectFuture.join();
            TeacherDTO teacher = teacherFuture.join();

            if (ordinarySubject == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid ordinary subject");
            }
            if (teacher == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid teacher");
            }
            return Result.success(new GroupRelationshipsDTO(teacher, ordinarySubject));
        }).join();
    }

    private Result<GroupRelationshipsDTO> validateElectiveSubjectRelationship(GroupInsertDTO groupInsertDTO) {
        if (groupInsertDTO.getElectiveSubjectId() == null) {
            return Result.error("Elective subject ID cannot be null.");
        }
        if (groupInsertDTO.getTeacherId() == null) {
            return Result.error("Teacher ID cannot be null.");
        }

        CompletableFuture<ElectiveSubjectDTO> electiveSubjectFuture = subjectFacadeService.getElectiveSubjectById(groupInsertDTO.getElectiveSubjectId());
        CompletableFuture<TeacherDTO> teacherFuture = teacherFacadeService.getTeacherById(groupInsertDTO.getTeacherId());

        return CompletableFuture.allOf(electiveSubjectFuture, teacherFuture).thenApply(v -> {
            ElectiveSubjectDTO electiveSubject = electiveSubjectFuture.join();
            TeacherDTO teacher = teacherFuture.join();

            if (electiveSubject == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid elective subject");
            }
            if (teacher == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid teacher");
            }
            return Result.success(new GroupRelationshipsDTO(teacher, electiveSubject));
        }).join();
    }
}

