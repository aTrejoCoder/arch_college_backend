package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.OrdinaryGroupInsertDTO;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.FacadeService.Teacher.TeacherFacadeService;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ExternalRelationsService {

    private final SubjectFacadeService subjectFacadeService;
    private final TeacherFacadeService teacherFacadeService;

    @Autowired
    public ExternalRelationsService(@Qualifier("subjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService,
                                    @Qualifier("teacherFacadeServiceImpl") TeacherFacadeService teacherFacadeService) {
        this.subjectFacadeService = subjectFacadeService;
        this.teacherFacadeService = teacherFacadeService;
    }

    public void addRelationshipSubject(Group group, GroupRelationshipsDTO relationshipsDTO) {
        if (relationshipsDTO.getElectiveSubjectDTO() != null) {
            ElectiveSubjectDTO electiveSubject = relationshipsDTO.getElectiveSubjectDTO();

            group.setSubjectName(electiveSubject.getName());
            group.setElectiveSubjectId(electiveSubject.getId());
        } else if (relationshipsDTO.getOrdinarySubjectDTO() != null) {
            OrdinarySubjectDTO ordinarySubject = relationshipsDTO.getOrdinarySubjectDTO();

            group.setSubjectName(ordinarySubject.getName() + " " + ordinarySubject.getNumber());
            group.setOrdinarySubjectId(ordinarySubject.getId());
        }
    }

    public Result<GroupRelationshipsDTO> validateAndGetRelationships(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO) {
            return validateOrdinarySubjectGroup(ordinaryGroupInsertDTO);
    }

    public Result<GroupRelationshipsDTO> validateAndGetRelationships(ElectiveGroupInsertDTO electiveGroupInsertDTO) {
        return validateElectiveSubjectGroup(electiveGroupInsertDTO);
    }

    private Result<GroupRelationshipsDTO> validateElectiveSubjectGroup(ElectiveGroupInsertDTO electiveGroupInsertDTO) {
        CompletableFuture<ElectiveSubjectDTO> electiveSubjectFuture = subjectFacadeService.getElectiveSubjectById(electiveGroupInsertDTO.getElectiveSubjectId());

        if(electiveGroupInsertDTO.getTeacherId() != null) {
            CompletableFuture<TeacherDTO> teachersFuture = teacherFacadeService.getTeacherById(electiveGroupInsertDTO.getTeacherId());

            return CompletableFuture.allOf(teachersFuture , electiveSubjectFuture).thenApply(v  -> {
                ElectiveSubjectDTO ordinarySubjectDTO = electiveSubjectFuture.join();
                TeacherDTO teacherDTO = teachersFuture.join();

                if (teacherDTO == null) {
                    return Result.<GroupRelationshipsDTO>error( "Invalid teacher");
                }

                if (ordinarySubjectDTO == null) {
                    return Result.<GroupRelationshipsDTO>error("Invalid ordinary subject");
                }

                return Result.success(new GroupRelationshipsDTO(ordinarySubjectDTO, teacherDTO));
            }).join();
        } else  {
            ElectiveSubjectDTO electiveSubjectDTO = electiveSubjectFuture.join();

            if (electiveSubjectDTO == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid ordinary subject");
            }

            return Result.success(new GroupRelationshipsDTO(electiveSubjectDTO));
        }
    }


    private Result<GroupRelationshipsDTO> validateOrdinarySubjectGroup(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO) {
        CompletableFuture<OrdinarySubjectDTO> electiveSubjectFuture = subjectFacadeService.getOrdinarySubjectById(ordinaryGroupInsertDTO.getOrdinarySubjectId());

        if(!ordinaryGroupInsertDTO.getTeacherIds().isEmpty()) {
            CompletableFuture<Result<List<TeacherDTO>>> teachersFuture = teacherFacadeService.getTeachersById(ordinaryGroupInsertDTO.getTeacherIds());

            return CompletableFuture.allOf(teachersFuture , electiveSubjectFuture).thenApply(v  -> {
                OrdinarySubjectDTO ordinarySubjectDTO = electiveSubjectFuture.join();
                Result<List<TeacherDTO>> teacherResult = teachersFuture.join();

                if (!teacherResult.isSuccess()) {
                    return Result.<GroupRelationshipsDTO>error(teacherResult.getErrorMessage());
                }

                if (ordinarySubjectDTO == null) {
                    return Result.<GroupRelationshipsDTO>error("Invalid ordinary subject");
                }

                return Result.success(new GroupRelationshipsDTO(ordinarySubjectDTO, teacherResult.getData()));
            }).join();
        } else  {
            OrdinarySubjectDTO ordinarySubjectDTO = electiveSubjectFuture.join();

            if (ordinarySubjectDTO == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid ordinary subject");
            }

            return Result.success(new GroupRelationshipsDTO(ordinarySubjectDTO));
        }
    }



}