package microservice.schedule_service.Service;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.common_classes.Utils.SubjectType;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeyGenerationService {

    private static final String STATIC_NUMBER_ORDINARY_START_KEY = "5";
    private static final String STATIC_NUMBER_ELECTIVE_START_KEY = "6";

    private final GroupRepository groupRepository;

    @Autowired
    public KeyGenerationService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // Obligatory Key example --> 5210
    // (5) means a generic number
    // (2) means the number of the semester that the semester that subject belongs
    // (10) means the number of the group from some subject
    public String generate(Group group, ObligatorySubjectDTO ordinarySubjectDTO) {
        Long subjectId = ordinarySubjectDTO.getId();

        List<Group> ordinaryGroups = groupRepository.findBySubjectIdAndSubjectTypeAndSchoolPeriod(
                subjectId,
                SubjectType.OBLIGATORY,
                AcademicData.getCurrentSchoolPeriod());

        int semesterNumber = ordinarySubjectDTO.getSemester();
        int subjectGroupNumber = ordinaryGroups.size() + 1;

        return String.format("%s%d%02d", STATIC_NUMBER_ORDINARY_START_KEY, semesterNumber, subjectGroupNumber);
    }

    // Obligatory Key example --> 6102
    // (6) means a generic number
    // (10) means the ID of the elective subject
    // (9) means the number of the group from the elective subject
    public String generate(Group group, ElectiveSubjectDTO electiveSubjectDTO) {
        Long subjectId = electiveSubjectDTO.getId();

        List<Group> electiveGroups = groupRepository.findBySubjectIdAndSubjectTypeAndSchoolPeriod(
                subjectId,
                SubjectType.ELECTIVE,
                AcademicData.getCurrentSchoolPeriod());

        int subjectGroupNumber = electiveGroups.size() + 1;

        return String.format("%s%02d%d", STATIC_NUMBER_ELECTIVE_START_KEY, electiveSubjectDTO.getId(), subjectGroupNumber);
    }
}
