package microservice.schedule_service.Mapppers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import microservice.schedule_service.DTO.GroupDTO;
import microservice.schedule_service.DTO.GroupInsertDTO;
import microservice.schedule_service.DTO.GroupUpdateDTO;
import microservice.schedule_service.DTO.ScheduleInsertDTO;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.Schedule;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-08T12:42:38-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.9.jar, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class GroupMapperImpl implements GroupMapper {

    @Override
    public Group insertDtoToEntity(GroupInsertDTO groupInsertDTO) {
        if ( groupInsertDTO == null ) {
            return null;
        }

        Group group = new Group();

        group.setOrdinarySubjectId( groupInsertDTO.getOrdinarySubjectId() );
        group.setElectiveSubjectId( groupInsertDTO.getElectiveSubjectId() );
        group.setTeacherId( groupInsertDTO.getTeacherId() );
        group.setSpots( groupInsertDTO.getSpots() );
        group.setClassroom( groupInsertDTO.getClassroom() );
        group.setSchedule( scheduleInsertDTOListToScheduleList( groupInsertDTO.getSchedule() ) );

        return group;
    }

    @Override
    public Group updateDtoToEntity(GroupUpdateDTO groupUpdateDTO) {
        if ( groupUpdateDTO == null ) {
            return null;
        }

        Group group = new Group();

        group.setTeacherId( groupUpdateDTO.getTeacherId() );
        group.setSpots( groupUpdateDTO.getSpots() );
        group.setClassroom( groupUpdateDTO.getClassroom() );
        group.setSchedule( scheduleInsertDTOListToScheduleList( groupUpdateDTO.getSchedule() ) );

        return group;
    }

    @Override
    public GroupDTO entityToDTO(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupDTO groupDTO = new GroupDTO();

        groupDTO.setId( group.getId() );
        groupDTO.setTeacherId( group.getTeacherId() );
        groupDTO.setSpots( group.getSpots() );
        groupDTO.setSchedule( scheduleListToScheduleInsertDTOList( group.getSchedule() ) );
        groupDTO.setClassroom( group.getClassroom() );

        return groupDTO;
    }

    protected Schedule scheduleInsertDTOToSchedule(ScheduleInsertDTO scheduleInsertDTO) {
        if ( scheduleInsertDTO == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setDay( scheduleInsertDTO.getDay() );
        schedule.setStartTime( scheduleInsertDTO.getStartTime() );
        schedule.setEndTime( scheduleInsertDTO.getEndTime() );

        return schedule;
    }

    protected List<Schedule> scheduleInsertDTOListToScheduleList(List<ScheduleInsertDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Schedule> list1 = new ArrayList<Schedule>( list.size() );
        for ( ScheduleInsertDTO scheduleInsertDTO : list ) {
            list1.add( scheduleInsertDTOToSchedule( scheduleInsertDTO ) );
        }

        return list1;
    }

    protected ScheduleInsertDTO scheduleToScheduleInsertDTO(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        ScheduleInsertDTO scheduleInsertDTO = new ScheduleInsertDTO();

        scheduleInsertDTO.setDay( schedule.getDay() );
        scheduleInsertDTO.setStartTime( schedule.getStartTime() );
        scheduleInsertDTO.setEndTime( schedule.getEndTime() );

        return scheduleInsertDTO;
    }

    protected List<ScheduleInsertDTO> scheduleListToScheduleInsertDTOList(List<Schedule> list) {
        if ( list == null ) {
            return null;
        }

        List<ScheduleInsertDTO> list1 = new ArrayList<ScheduleInsertDTO>( list.size() );
        for ( Schedule schedule : list ) {
            list1.add( scheduleToScheduleInsertDTO( schedule ) );
        }

        return list1;
    }
}
