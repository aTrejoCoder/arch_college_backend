package microservice.grade_service.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.GroupEnrollmentDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.grade_service.DTOs.GroupDTO;
import microservice.grade_service.DTOs.TeacherQualificationDTO;
import microservice.grade_service.Mappers.GroupMapper;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Repository.GroupRepository;
import microservice.grade_service.Repository.SubjectRepository;
import microservice.grade_service.Service.AcademicHistoryService;
import microservice.grade_service.Service.GroupService;
import microservice.grade_service.Utils.GradeValues;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GradeRepository gradeRepository;
    private final AcademicHistoryService academicHistoryService;

    @Override
    public Group createGroupFromEnrollment(GroupEnrollmentDTO groupEnrollmentDTO, Subject subject) {
        log.info("Creating group from enrollment: GroupId={}, SubjectId={}", groupEnrollmentDTO.getGroupId(), subject.getSubjectId());
        Group group = new Group();
        group.setGroupId(groupEnrollmentDTO.getGroupId());
        group.setGroupType(groupEnrollmentDTO.getGroupType());
        group.setHeadTeacherAccountNumber(groupEnrollmentDTO.getHeadTeacherAccountNumber());
        group.setSubject(subject);

        groupRepository.saveAndFlush(group);
        log.info("Group created successfully: {}", group);
        return group;
    }

    @Override
    public Result<GroupDTO> getGroupById(Long groupId) {
        log.info("Fetching group by ID: {}", groupId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.map(group -> {
            log.info("Group found: {}", group);
            return Result.success(groupMapper.entityToDTO(group));
        }).orElseGet(() -> {
            return Result.error("Group with ID " + groupId + " not found");
        });
    }

    @Override
    public Page<GroupDTO> getPendingGroups(Pageable pageable) {
        Page<Group> groupPage = groupRepository.findByIsGroupQualifiedFalse(pageable);
        return groupPage.map(groupMapper::entityToDTO);
    }

    @Override
    public List<GroupDTO> getTeacherGroupsPendingToBeQualified(String teacherAccountNumber) {
        List<Group> groups = groupRepository.findByHeadTeacherAccountNumberAndIsGroupQualifiedFalse(teacherAccountNumber);
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    public List<GroupDTO> getTeacherGroupsQualified(String teacherAccountNumber) {
        List<Group> groups = groupRepository.findByHeadTeacherAccountNumberAndIsGroupQualifiedTrue(teacherAccountNumber)
                .stream()
                .sorted(Comparator.comparing(Group::getQualifiedAt))
                .toList();
        return groups.stream().map(groupMapper::entityToDTO).toList();
    }

    @Override
    public Result<Void> validateGroupQualification(TeacherQualificationDTO teacherQualificationDTO, String teacherAccountNumber) {
        Group group = groupRepository.findByHeadTeacherAccountNumberAndGroupId(teacherAccountNumber, teacherQualificationDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group with Head Teacher (" + teacherAccountNumber + ") Not Found"));

        if (group.isGroupQualified()) {
            log.warn("Group already qualified: {}", group);
            return Result.error("Group Already Qualified");
        }

        Set<String> studentAccountNumbers = group.getStudentAccountNumbers();
        Set<String> studentAccountNumbersProvided = teacherQualificationDTO.getStudentGradeMap().keySet();

        Set<String> notProvidedStudentAccountNumbers = new HashSet<>(studentAccountNumbers);
        notProvidedStudentAccountNumbers.removeAll(studentAccountNumbersProvided);

        if (!notProvidedStudentAccountNumbers.isEmpty()) {
            return Result.error("Not Grade Provided for Students: " + notProvidedStudentAccountNumbers);
        }

        return Result.success();
    }

    @Override
    public Result<Void> validateGroupGradingPeriodTime() {
        var startPeriodTime = AcademicData.getGradingStartPeriodTime();
        var endPeriodTime = AcademicData.getGradingEndPeriodTime();
        var now = LocalDateTime.now();

        if (now.isBefore(endPeriodTime) && now.isAfter(startPeriodTime)) {
            return Result.success();
        } else {
            return Result.error("System Closed. Only Open in January, June, July and December");
        }
    }

    @Override
    public void addGroupQualifications(TeacherQualificationDTO teacherQualificationDTO, String teacherAccountNumber) {
        log.info("Adding qualifications to group: GroupId={}, TeacherAccountNumber={}", teacherQualificationDTO.getGroupId(), teacherAccountNumber);
        Group group = groupRepository.findByHeadTeacherAccountNumberAndGroupId(teacherAccountNumber, teacherQualificationDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group Not Found"));

        Map<String, GradeValues> studentGradeValues = teacherQualificationDTO.getStudentGradeMap();
        List<Grade> grades = group.getGrades();

        grades.forEach(grade -> {
            GradeValues gradeValue = studentGradeValues.get(grade.getStudentAccountNumber());
            if (gradeValue == GradeValues.NO_PRESENT) {
                grade.setAsNotPresent();
            } else {
                grade.rate(gradeValue.getNumericValue());
            }
        });

        gradeRepository.saveAll(grades);
        group.setGroupQualified(true);
        group.setQualifiedAt(LocalDateTime.now());
        groupRepository.save(group);
        log.info("Group qualifications added successfully");
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public void addGradesToAcademicHistoryAsync(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group Not Found"));
        Hibernate.initialize(group.getGrades());

        group.getGrades().forEach(academicHistoryService::setGradeToAcademicHistory);
        log.info("addGradesToAcademicHistoryAsync -> Grades added to academic history for group: GroupId={}", groupId);
    }

    @Override
    public void undoGroupQualifications(Long groupId, String teacherAccountNumber) {
        Group group = groupRepository.findByHeadTeacherAccountNumberAndGroupId(teacherAccountNumber, groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group Not Found"));

        if (!group.isGroupQualified()) {
            throw new IllegalStateException("Group is already unqualified");
        }

        group.getGrades().forEach(Grade::setAsNotRated);
        group.setGroupQualified(false);
        group.setQualifiedAt(null);

        groupRepository.save(group);
        gradeRepository.saveAll(group.getGrades());
        log.info("undoGroupQualifications --> Qualifications for group: GroupId={}, TeacherAccountNumber={} successfully undo it", groupId, teacherAccountNumber);
    }
}
