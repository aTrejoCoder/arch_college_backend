package microservice.schedule_service.Service.GroupServices;

import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.OrdinaryGroupInsertDTO;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.FacadeService.Teacher.TeacherFacadeService;
import microservice.common_classes.Utils.Result;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.Teacher;
import microservice.schedule_service.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GroupRelationshipService {
    private final SubjectFacadeService subjectFacadeService;
    private final TeacherFacadeService teacherFacadeService;
    private final TeacherRepository teacherRepository;

    @Autowired
    public GroupRelationshipService(
            @Qualifier("SubjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService,
            @Qualifier("TeacherFacadeServiceImpl") TeacherFacadeService teacherFacadeService,
            TeacherRepository teacherRepository) {
        this.subjectFacadeService = subjectFacadeService;
        this.teacherFacadeService = teacherFacadeService;
        this.teacherRepository = teacherRepository;
    }

    public Result<GroupRelationshipsDTO> validateAndGetRelationships(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO) {
        return validateOrdinarySubjectGroup(ordinaryGroupInsertDTO);
    }

    public Result<GroupRelationshipsDTO> validateAndGetRelationships(ElectiveGroupInsertDTO electiveGroupInsertDTO) {
        return validateElectiveSubjectGroup(electiveGroupInsertDTO);
    }

    public void addRelationshipSubject(Group group, GroupRelationshipsDTO relationshipsDTO) {
        if (relationshipsDTO.getElectiveSubjectDTO() != null) {
            ElectiveSubjectDTO electiveSubject = relationshipsDTO.getElectiveSubjectDTO();

            group.setSubjectName(electiveSubject.getName());
            group.setElectiveSubjectId(electiveSubject.getId());
        } else if (relationshipsDTO.getObligatorySubjectDTO() != null) {
            ObligatorySubjectDTO ordinarySubject = relationshipsDTO.getObligatorySubjectDTO();

            group.setSubjectName(ordinarySubject.getName() + " " + ordinarySubject.getNumber());
            group.setObligatorySubjectId(ordinarySubject.getId());
        }
    }

    public void setExternalGroupRelationships(Group group, GroupRelationshipsDTO groupRelationshipsDTO){
        addRelationshipSubject(group, groupRelationshipsDTO);

        if (groupRelationshipsDTO.getTeacherDTOS() != null) {
            List<Teacher> teachers = groupRelationshipsDTO.getTeacherDTOS().stream()
                    .map(teacherDTO -> teacherRepository.findById(teacherDTO.getTeacherId())
                            .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherDTO.getTeacherId())))
                    .toList();

            group.addTeachers(teachers);
        }
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
        CompletableFuture<ObligatorySubjectDTO> obligatorySubjectFuture = subjectFacadeService.getOrdinarySubjectById(ordinaryGroupInsertDTO.getOrdinarySubjectId());

        if(!ordinaryGroupInsertDTO.getTeacherIds().isEmpty()) {
            CompletableFuture<Result<List<TeacherDTO>>> teachersFuture = teacherFacadeService.getTeachersById(ordinaryGroupInsertDTO.getTeacherIds());

            return CompletableFuture.allOf(teachersFuture , obligatorySubjectFuture).thenApply(v  -> {
                ObligatorySubjectDTO ordinarySubjectDTO = obligatorySubjectFuture.join();
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
            ObligatorySubjectDTO ordinarySubjectDTO = obligatorySubjectFuture.join();

            if (ordinarySubjectDTO == null) {
                return Result.<GroupRelationshipsDTO>error("Invalid ordinary subject");
            }

            return Result.success(new GroupRelationshipsDTO(ordinarySubjectDTO));
        }
    }

}