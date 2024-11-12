package microservice.schedule_service.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.common_classes.DTOs.Group.*;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.DTOs.Teacher.TeacherDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.FacadeService.Teacher.TeacherFacadeService;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.common_classes.Utils.Title;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.cache.annotation.Cacheable;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final SubjectFacadeService subjectFacadeService;
    private final ScheduleValidationService scheduleValidationService;
    private final KeyGenerationService keyGenerationService;
    private final TeacherFacadeService teacherFacadeService;
    private final GroupMapper groupMapper;
    private final String currentSemester = SemesterData.getCurrentSemester();

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            @Qualifier("subjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService,
                            ScheduleValidationService scheduleValidationService,
                            KeyGenerationService keyGenerationService,
                            @Qualifier("teacherFacadeServiceImpl") TeacherFacadeService teacherFacadeService,
                            GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.subjectFacadeService = subjectFacadeService;
        this.scheduleValidationService = scheduleValidationService;
        this.keyGenerationService = keyGenerationService;
        this.teacherFacadeService = teacherFacadeService;
        this.groupMapper = groupMapper;
    }


    @Override
    @Cacheable(value = "groupById", key = "#groupId")
    public Result<GroupDTO> getGroupById(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.map(group -> Result.success(groupMapper.entityToDTO(group)))
                .orElseGet(() -> Result.error("Group with Id " + groupId + " not found"));
    }

    @Override
    @Cacheable(value = "groupCurrentByKey", key = "#key")
    public Result<GroupDTO> getGroupCurrentByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester);
        return optionalGroup.map(group -> Result.success(groupMapper.entityToDTO(group)))
                .orElseGet(() -> Result.error("Group with Key " + key + " not found"));
    }

    @Override
    @Cacheable(value = "currentGroupsBySubjectId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsBySubjectId(Long subjectId) {
        List<Group> groups = groupRepository.findByOrdinarySubjectId(subjectId);


        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    @Cacheable(value = "currentGroupsByTeacherId", key = "#subjectId")
    public List<GroupDTO> getCurrentGroupsByTeacherId(Long subjectId) {
        List<Group> groups = groupRepository.findByTeacherIdAndSchoolPeriod(subjectId, currentSemester);
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    @Transactional
    public GroupDTO createGroup(GroupInsertDTO groupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(groupInsertDTO);

        group.setSchoolPeriod(SemesterData.getCurrentSemester());
        addGroupRelationShipData(group, groupRelationshipsDTO);
        group.setKey(keyGenerationService.generateKey(group, groupInsertDTO));

        groupRepository.saveAndFlush(group);

        return groupMapper.entityToDTO(group);
    }

    // TO BE FIXED
    @Override
    public void updateGroup(GroupUpdateDTO groupUpdateDTO) {
        Group group = groupMapper.updateDtoToEntity(groupUpdateDTO);
        groupRepository.save(group);
    }

    @Override
    public void deleteCurrentGroupByKey(String key) {
        Optional<Group> optionalGroup = groupRepository.findByKeyAndSchoolPeriod(key, currentSemester);
        if (optionalGroup.isEmpty()) {
            throw new EntityNotFoundException("Group with Key " + key + " not found");
        }

        groupRepository.delete(optionalGroup.get());
    }


    @Override
    public Result<Void> validateGroupSchedule(String classroom, List<ScheduleInsertDTO> schedules) {
        return scheduleValidationService.validateSchedule(classroom, schedules, SemesterData.getCurrentSemester());
    }

    @Override
    public Result<GroupRelationshipsDTO> getGroupRelationshipsData(GroupInsertDTO groupInsertDTO) {
        if (groupInsertDTO.getOrdinarySubjectId() != null && groupInsertDTO.getElectiveSubjectId() == null) {
            CompletableFuture<OrdinarySubjectDTO> ordinarySubjectFuture = subjectFacadeService.getOrdinarySubjectById(groupInsertDTO.getOrdinarySubjectId());
            CompletableFuture<TeacherDTO> exisitingTeacherFuture =  teacherFacadeService.getTeacherById(groupInsertDTO.getTeacherId());

            return CompletableFuture.allOf(ordinarySubjectFuture, exisitingTeacherFuture).thenApply(v -> {
                OrdinarySubjectDTO ordinarySubject = ordinarySubjectFuture.join();
                TeacherDTO teacherSubject = exisitingTeacherFuture.join();

                if (ordinarySubject == null) {
                    return Result.<GroupRelationshipsDTO>error("invalid ordinary subject");
                }

                if (teacherSubject == null) {
                    return Result.<GroupRelationshipsDTO>error("invalid teacher");
                }

                GroupRelationshipsDTO groupRelationshipsDTO = new GroupRelationshipsDTO(teacherSubject,ordinarySubject);
                return Result.success(groupRelationshipsDTO);
            }).join();

        } else if (groupInsertDTO.getOrdinarySubjectId() == null && groupInsertDTO.getElectiveSubjectId() != null) {
            CompletableFuture<ElectiveSubjectDTO> electiveSubjectFuture = subjectFacadeService.getElectiveSubjectById(groupInsertDTO.getElectiveSubjectId());
            CompletableFuture<TeacherDTO> exisitingTeacherFuture =  teacherFacadeService.getTeacherById(groupInsertDTO.getTeacherId());

            return CompletableFuture.allOf(electiveSubjectFuture, exisitingTeacherFuture).thenApply(v -> {
                ElectiveSubjectDTO electiveSubject = electiveSubjectFuture.join();
                TeacherDTO teacherDTO = exisitingTeacherFuture.join();

                if (electiveSubject == null) {
                    return Result.<GroupRelationshipsDTO>error("invalid elective subject");
                }

                if (teacherDTO == null) {
                    return Result.<GroupRelationshipsDTO>error("invalid teacher");
                }

                GroupRelationshipsDTO groupRelationshipsDTO = new GroupRelationshipsDTO(teacherDTO,electiveSubject);
                return Result.success(groupRelationshipsDTO);
            }).join();
        } else {
                return Result.error("No Subject Id provided");
        }
    }

    private void addGroupRelationShipData(Group group, GroupRelationshipsDTO groupRelationshipsDTO) {
         TeacherDTO teacherDTO = groupRelationshipsDTO.getTeacherDTO();
         group.setTeacherId(teacherDTO.getTeacherId());
         group.setTeacherName("+" + teacherDTO.getTitle().getInitials() + " " + teacherDTO.getFirstName() + " " + teacherDTO.getLastName());

         if (groupRelationshipsDTO.getElectiveSubjectDTO() != null) {
             ElectiveSubjectDTO electiveSubjectDTO = groupRelationshipsDTO.getElectiveSubjectDTO();
             group.setSubjectName(electiveSubjectDTO.getName());
             group.setElectiveSubjectId(electiveSubjectDTO.getId());
             return;
         }

        if (groupRelationshipsDTO.getOrdinarySubjectDTO() != null) {
            OrdinarySubjectDTO ordinarySubjectDTO = groupRelationshipsDTO.getOrdinarySubjectDTO();
            group.setSubjectName(ordinarySubjectDTO.getName() + " " + ordinarySubjectDTO.getNumber());
            group.setOrdinarySubjectId(ordinarySubjectDTO.getId());
        }
    }
}

