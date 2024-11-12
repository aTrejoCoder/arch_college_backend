package microservice.schedule_service.Service;

import microservice.common_classes.DTOs.Group.GroupInsertDTO;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyGenerationService {

    private static final String STATIC_NUMBER_ORDINARY_START_KEY = "5";
    private static final String STATIC_NUMBER_ELECTIVE_START_KEY = "6";

    private final GroupRepository groupRepository;
    private final SubjectFacadeService subjectFacadeService;

    @Autowired
    public KeyGenerationService(GroupRepository groupRepository,
                                @Qualifier("subjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService) {
        this.groupRepository = groupRepository;
        this.subjectFacadeService = subjectFacadeService;
    }

    public String generateKey(Group group, GroupInsertDTO groupInsertDTO) {
        if (groupInsertDTO.getElectiveSubjectId() != null && groupInsertDTO.getOrdinarySubjectId() == null) {
            return generateElectiveKey(group);
        } else if (groupInsertDTO.getOrdinarySubjectId() != null && groupInsertDTO.getElectiveSubjectId() == null) {
            return generateOrdinaryKey(group);
        } else {
            throw new RuntimeException("Invalid request, only one subject id allowed");
        }
    }

    // Ordinary Key example --> 5210
    // (5) means a generic number
    // (2) means the number of the semester that the semester that subject belongs
    // (10) means the number of the group from some subject
    private String generateOrdinaryKey(Group group) {
        List<Group> ordinaryGroups = groupRepository.findByOrdinarySubjectId(group.getOrdinarySubjectId());

        var subject = subjectFacadeService.getOrdinarySubjectById(group.getOrdinarySubjectId()).join();

        int semesterNumber = subject.getSemesterNumber();
        int subjectGroupNumber = ordinaryGroups.size() + 1;

        return String.format("%s%d%02d", STATIC_NUMBER_ORDINARY_START_KEY, semesterNumber, subjectGroupNumber);
    }

    // Ordinary Key example --> 6102
    // (6) means a generic number
    // (10) means the ID of the elective subject
    // (9) means the number of the group from the elective subject
    private String generateElectiveKey(Group group) {
        List<Group> electiveGroups = groupRepository.findByElectiveSubjectId(group.getElectiveSubjectId());

        var subject = subjectFacadeService.getElectiveSubjectById(group.getElectiveSubjectId()).join();
        int subjectGroupNumber = electiveGroups.size() + 1;

        return String.format("%s%02d%d", STATIC_NUMBER_ELECTIVE_START_KEY, subject.getId(), subjectGroupNumber);
    }
}
