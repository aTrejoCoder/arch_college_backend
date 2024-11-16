package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyGenerationService {

    private static final String STATIC_NUMBER_ORDINARY_START_KEY = "5";
    private static final String STATIC_NUMBER_ELECTIVE_START_KEY = "6";

    private final GroupRepository groupRepository;

    @Autowired
    public KeyGenerationService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // Ordinary Key example --> 5210
    // (5) means a generic number
    // (2) means the number of the semester that the semester that subject belongs
    // (10) means the number of the group from some subject
    public String generateOrdinaryKey(Group group, OrdinarySubjectDTO ordinarySubjectDTO) {
        List<Group> ordinaryGroups = groupRepository.findByOrdinarySubjectId(group.getOrdinarySubjectId());

        int semesterNumber = ordinarySubjectDTO.getSemesterNumber();
        int subjectGroupNumber = ordinaryGroups.size() + 1;

        return String.format("%s%d%02d", STATIC_NUMBER_ORDINARY_START_KEY, semesterNumber, subjectGroupNumber);
    }

    // Ordinary Key example --> 6102
    // (6) means a generic number
    // (10) means the ID of the elective subject
    // (9) means the number of the group from the elective subject
    public String generateElectiveKey(Group group, ElectiveSubjectDTO electiveSubjectDTO) {
        List<Group> electiveGroups = groupRepository.findByElectiveSubjectId(group.getElectiveSubjectId());

        int subjectGroupNumber = electiveGroups.size() + 1;

        return String.format("%s%02d%d", STATIC_NUMBER_ELECTIVE_START_KEY, electiveSubjectDTO.getId(), subjectGroupNumber);
    }
}
