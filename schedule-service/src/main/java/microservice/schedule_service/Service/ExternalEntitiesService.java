package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.GroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupRelationshipsDTO;
import microservice.common_classes.DTOs.Group.GroupScheduleUpdateDTO;
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

import java.util.concurrent.CompletableFuture;

@Service
public class ExternalEntitiesService {

    private final SubjectFacadeService subjectFacadeService;
    private final TeacherFacadeService teacherFacadeService;

    @Autowired
    public ExternalEntitiesService(@Qualifier("subjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService,
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

    public void addTeacher(Group group, GroupRelationshipsDTO groupRelationshipsDTO) {
        TeacherDTO teacherDTO = groupRelationshipsDTO.getTeacherDTO();
        if (teacherDTO != null) {
            group.setTeacherId(teacherDTO.getTeacherId());
            group.setTeacherName(teacherDTO.getTitle().getInitials() + " " + teacherDTO.getFirstName() + " " + teacherDTO.getLastName());
        }
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
        CompletableFuture<OrdinarySubjectDTO> ordinarySubjectAsync = subjectFacadeService.getOrdinarySubjectById(groupInsertDTO.getOrdinarySubjectId());

        if (groupInsertDTO.getTeacherId() != null) {
            CompletableFuture<TeacherDTO> teacherFuture = teacherFacadeService.getTeacherById(groupInsertDTO.getTeacherId());

            return CompletableFuture.allOf(teacherFuture, ordinarySubjectAsync).thenApply(v -> {
                TeacherDTO teacherDTO = teacherFuture.join();
                OrdinarySubjectDTO ordinarySubjectDTO = ordinarySubjectAsync.join();

                if(teacherDTO == null) {
                    return Result.<GroupRelationshipsDTO>error("Invalid Teacher");
                }
                if(ordinarySubjectDTO == null) {
                    return Result.<GroupRelationshipsDTO>error("Invalid Ordinary Subject");
                }

                return Result.success(new GroupRelationshipsDTO(teacherDTO, ordinarySubjectDTO));
            }).join();
        } else {
            OrdinarySubjectDTO ordinarySubjectDTO = ordinarySubjectAsync.join();

            if(ordinarySubjectDTO == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid Ordinary Subject");
            }

            return Result.success(new GroupRelationshipsDTO(ordinarySubjectDTO));
        }
    }

    private Result<GroupRelationshipsDTO> validateElectiveSubjectRelationship(GroupInsertDTO groupInsertDTO) {
        CompletableFuture<ElectiveSubjectDTO> electiveSubjectFuture = subjectFacadeService.getElectiveSubjectById(groupInsertDTO.getElectiveSubjectId());

        if (groupInsertDTO.getTeacherId() != null) {
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
        } else {
            ElectiveSubjectDTO electiveSubject = electiveSubjectFuture.join();

            if (electiveSubject == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid elective subject");
            }

            return Result.success(new GroupRelationshipsDTO(electiveSubject));
        }
    }
}