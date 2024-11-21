package microservice.schedule_service.Service.GroupServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Group.ElectiveGroupInsertDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Group.OrdinaryGroupInsertDTO;
import microservice.common_classes.DTOs.Teacher.TeacherNameDTO;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.schedule_service.Mapppers.GroupMapper;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.GroupRelationshipsDTO;
import microservice.schedule_service.Repository.GroupRepository;
import microservice.schedule_service.Service.KeyGenerationService;
import microservice.schedule_service.Service.ScheduleService;
import microservice.schedule_service.Utils.GroupMappingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupCreationService {

    private GroupMapper groupMapper;
    private GroupMappingService mappingService;
    private ScheduleService scheduleService;
    private KeyGenerationService keyGenerationService;
    private GroupRelationshipService relationshipService;
    private final GroupRepository groupRepository;
    private final String currentSemester = SemesterData.getCurrentSemester();


    @Transactional
    public GroupDTO createGroup(OrdinaryGroupInsertDTO ordinaryGroupInsertDTO, GroupRelationshipsDTO groupRelationshipsDTO) {
        Group group = groupMapper.insertDtoToEntity(ordinaryGroupInsertDTO);

        group.setSchoolPeriod(currentSemester);
        group.initSpots(ordinaryGroupInsertDTO.getTotalSpots());
        group.setSchedule(scheduleService.mapScheduleDTOToEntity(ordinaryGroupInsertDTO.getSchedule()));
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
