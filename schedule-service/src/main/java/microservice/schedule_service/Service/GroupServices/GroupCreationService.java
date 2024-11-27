package microservice.schedule_service.Service.GroupServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.ObligatoryGroupInsertDTO;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Service.KeyGenerationService;
import microservice.schedule_service.Service.ScheduleService;
import microservice.schedule_service.Utils.GroupMappingService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCreationService {

    private final GroupMapper groupMapper;
    private final GroupMappingService mappingService;
    private final ScheduleService scheduleService;
    private final KeyGenerationService keyGenerationService;
    private final GroupRelationshipService relationshipService;
    private final GroupRepository groupRepository;
    private final String currentSemester = SemesterData.getCurrentSchoolPeriod();


    @Transactional
    public GroupDTO createGroup(ObligatoryGroupInsertDTO OBligatoryGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(OBligatoryGroupInsertDTO);

        group.setSchoolPeriod(currentSemester);
        group.initSpots(OBligatoryGroupInsertDTO.getTotalSpots());
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(OBligatoryGroupInsertDTO.getSchedule()));
        group.setKey(keyGenerationService.generateObligatoryKey(group, groupRelationshipsDTO.getObligatorySubjectDTO()));

        relationshipService.setExternalGroupRelationships(group, groupRelationshipsDTO);

        groupRepository.saveAndFlush(group);

        return mappingService.mapGroupToDTOWithTeachers(group);
    }

    @Transactional
    public GroupDTO createGroup(ElectiveGroupInsertDTO electiveGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(electiveGroupInsertDTO);

        group.setSchedule(scheduleService.mapScheduleDTOToEntity(electiveGroupInsertDTO.getSchedule()));
        group.setSchoolPeriod(currentSemester);

        if (groupRelationshipsDTO.getObligatorySubjectDTO() != null) {
            group.setKey(keyGenerationService.generateObligatoryKey(group, groupRelationshipsDTO.getObligatorySubjectDTO()));
        }

        if (groupRelationshipsDTO.getElectiveSubjectDTO() != null) {
            group.setKey(keyGenerationService.generateElectiveKey(group, groupRelationshipsDTO.getElectiveSubjectDTO()));
        }

        relationshipService.setExternalGroupRelationships(group, groupRelationshipsDTO);

        groupRepository.saveAndFlush(group);

        return mappingService.mapGroupToDTOWithTeachers(group);
    }
}
