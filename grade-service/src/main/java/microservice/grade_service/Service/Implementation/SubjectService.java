package microservice.grade_service.Service.Implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentDTO;
import microservice.common_classes.DTOs.Enrollment.GroupEnrollmentDTO;
import microservice.grade_service.Mappers.SubjectMapper;
import microservice.grade_service.Model.Subject;
import microservice.grade_service.Repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectService {
    private final SubjectMapper subjectMapper;
    private final SubjectRepository subjectRepository;

    public Subject ensureSubjectsExist(GroupEnrollmentDTO groupEnrollmentDTO) {
        Optional<Subject> optionalSubject = subjectRepository.findBySubjectIdAndSubjectType(groupEnrollmentDTO.getSubjectId(), groupEnrollmentDTO.getSubjectType());
        if (optionalSubject.isEmpty()) {
            Subject subject = new Subject();
            subject.setSubjectId(groupEnrollmentDTO.getSubjectId());
            subject.setSubjectId(groupEnrollmentDTO.getSubjectId());
            subject.setSubjectName(groupEnrollmentDTO.getSubjectName());
            subject.setSubjectType(groupEnrollmentDTO.getSubjectType());
            subject.setSubjectCredits(groupEnrollmentDTO.getSubjectCredits());

            subject = subjectRepository.saveAndFlush(subject);

            return subject;
        }

        return optionalSubject.get();
    }

}
