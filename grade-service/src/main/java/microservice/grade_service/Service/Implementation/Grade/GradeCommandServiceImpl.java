package microservice.grade_service.Service.Implementation.Grade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.Utils.Grades.GradeType;
import microservice.grade_service.Mappers.GradeMapper;
import microservice.grade_service.Mappers.GroupMapper;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.Group;
import microservice.grade_service.Model.Subject;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Repository.GroupRepository;
import microservice.grade_service.Repository.SubjectRepository;
import microservice.grade_service.Service.GradeCommandService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeCommandServiceImpl implements GradeCommandService {

    private final GroupMapper groupMapper;
    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;
    private final SubjectRepository subjectRepository;

    @Override
    public void initGradesFromEnrollments(Group group, List<EnrollmentDTO> enrollmentDTOS) {
        List<Grade> grades = enrollmentDTOS.stream()
                .map(enrollmentDTO -> {
                    Subject subject = group.getSubject();

                    GradeType gradeType = GradeType.CREDITABLE;
                    if (subject.getSubjectCredits() == 0) {
                        gradeType = GradeType.NO_CREDITS;
                    }

                    return Grade.builder()
                            .gradeStatus(Grade.GradeStatus.PENDING_RESULT)
                            .schoolPeriod(enrollmentDTO.getSchoolPeriod())
                            .studentAccountNumber(enrollmentDTO.getStudentAccountNumber())
                            .gradeType(gradeType)
                            .subject(subject)
                            .group(group)
                            .build();
                })
                 .toList();

        gradeRepository.saveAll(grades);
    }

    @Override
    public void deleteGradeById(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new NotFoundException("Grade with ID " + gradeId + " not found"));

        grade.setAsDeleted();
        gradeRepository.save(grade);
    }
}
