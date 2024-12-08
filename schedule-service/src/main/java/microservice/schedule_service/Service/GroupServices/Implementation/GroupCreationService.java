package microservice.schedule_service.Service.GroupServices.Implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.ObligatoryGroupInsertDTO;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Service.KeyGenerationService;
import microservice.schedule_service.Service.ScheduleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupCreationService {

    private final GroupMapper groupMapper;
    private final ScheduleService scheduleService;
    private final KeyGenerationService keyGenerationService;
    private final GroupRelationshipService relationshipService;
    private final GroupRepository groupRepository;
    private final String currentSemester = AcademicData.getCurrentSchoolPeriod();

    @Transactional
    public GroupDTO createGroup(ObligatoryGroupInsertDTO ObligatoryGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(ObligatoryGroupInsertDTO);

        group.setSchoolPeriod(currentSemester);
        group.initSpots(ObligatoryGroupInsertDTO.getTotalSpots());
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(ObligatoryGroupInsertDTO.getSchedule()));

        group.setGroupKey(keyGenerationService.generate(group, groupRelationshipsDTO.getObligatorySubjectDTO()));

        relationshipService.setGroupRelationships(group, groupRelationshipsDTO);

        group.setHeadTeacherFromTeachers(ObligatoryGroupInsertDTO.getHeadTeacherId());

        groupRepository.saveAndFlush(group);
        log.info("createGroup -> Group saved successfully with ID: {}", group.getId());

        return groupMapper.entityToDTO(group);
    }

    @Transactional
    public GroupDTO createGroup(ElectiveGroupInsertDTO electiveGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(electiveGroupInsertDTO);
        log.debug("Mapped group entity: {}", group);

        group.setSchedule(scheduleService.mapScheduleDTOToEntity(electiveGroupInsertDTO.getSchedule()));
        group.setSchoolPeriod(currentSemester);

        if (groupRelationshipsDTO.getObligatorySubjectDTO() != null) {
            group.setGroupKey(keyGenerationService.generate(group, groupRelationshipsDTO.getObligatorySubjectDTO()));
        }

        if (groupRelationshipsDTO.getElectiveSubjectDTO() != null) {
            group.setGroupKey(keyGenerationService.generate(group, groupRelationshipsDTO.getElectiveSubjectDTO()));
        }

        relationshipService.setGroupRelationships(group, groupRelationshipsDTO);

        groupRepository.saveAndFlush(group);
        log.info("Group created with ID: {}", group.getId());

        return groupMapper.entityToDTO(group);
    }
}
