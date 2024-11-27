package microservice.enrollment_service.Service;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;
import microservice.enrollment_service.Model.GroupEnrollment;
import microservice.enrollment_service.Repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EnrollmentValidationServiceImpl implements EnrollmentValidationService {
    public final int MAX_CREDITS_PER_SCHOOL_PERIOD = 120;
    private static final String ERROR_ELECTIVES_COMPLETED = "You have already completed all your elective subjects. Enrollment in additional electives is not allowed.";
    private static final String ERROR_FREE_ELECTIVES_LIMIT = "You have reached the limit for free electives from other professional lines. You can only enroll in electives from your professional line.";
    private final EnrollmentRepository enrollmentRepository;
    private final String schoolPeriod = SemesterData.getCurrentSchoolPeriod();

    public EnrollmentValidationServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Result<Void> validateEnrollment(EnrollmentInsertDTO enrollmentInsertDTO, EnrollmentRelationshipDTO enrollmentRelationshipDTO, String accountNumber) {
        List<GroupEnrollment> groupEnrollments = enrollmentRepository.findByStudentAccountNumberAndEnrollmentPeriod(accountNumber, schoolPeriod);

        Result<Void> notDuplicatedResult = validateNotDuplicatedEnrollment(enrollmentInsertDTO.getGroupKey(), groupEnrollments);
        if (!notDuplicatedResult.isSuccess()) {
            return Result.error(notDuplicatedResult.getErrorMessage());
        }

        Result<Void> validateSubjectResult = validateSubject(enrollmentRelationshipDTO);
        if (!validateSubjectResult.isSuccess()) {
            return Result.error(validateSubjectResult.getErrorMessage());
        }

        Result<Void> maxCreditsResult = validateMaximumCreditsPerStudent(accountNumber, enrollmentRelationshipDTO);
        if (!maxCreditsResult.isSuccess()) {
            return Result.error(maxCreditsResult.getErrorMessage());
        }

        Result<Void> gradeConflictResult = validateNotGradeConflict(enrollmentRelationshipDTO);
        if (!gradeConflictResult.isSuccess()) {
            return Result.error(gradeConflictResult.getErrorMessage());
        }

        return Result.success();
    }

    private Result<Void> validateNotGradeConflict(EnrollmentRelationshipDTO enrollmentRelationshipDTO) {
         List<GradeDTO> studentGrades = enrollmentRelationshipDTO.getStudentGrades();

         if (enrollmentRelationshipDTO.getObligatorySubjectDTO() != null) {
             List<GradeDTO> gradesPerSubject = studentGrades.stream()
                     .filter(gradeDTO -> gradeDTO.getOrdinarySubjectId()
                     .equals(enrollmentRelationshipDTO.getObligatorySubjectDTO().getId()))
                     .toList();

             // Doesn't have grades for this subject, won't have conflicts
             if(gradesPerSubject.isEmpty()) {
                 return Result.success();
             }

             int notApprovedCount = 0;
             for (var grade : gradesPerSubject) {
                 // Not Approved
                 if (grade.getGradeValue() < 6) {
                     notApprovedCount++;
                 } else {
                     // Approved
                     return Result.error("Can't make enrollment, subject already approved");
                 }
             }

             if (notApprovedCount >= 3) {
                 return Result.error("This subject has been not approved more than twice. From now this subject can only be pass with an extraordinary exam");
             }

             return Result.success();
         }



        return Result.success();
    }

    private Result<Void> validateMaximumCreditsPerStudent(String accountNumber, EnrollmentRelationshipDTO enrollmentRelationshipDTO) {
        List<GroupEnrollment> currentStudentEnrollments = enrollmentRepository.findByStudentAccountNumberAndEnrollmentPeriod(accountNumber, schoolPeriod);
        int currentTotalCredits = 0;

        for (var studentEnrollment : currentStudentEnrollments) {
            currentTotalCredits += studentEnrollment.getSubjectCredits();
        }

        if (currentTotalCredits > MAX_CREDITS_PER_SCHOOL_PERIOD) {
            return Result.error("Can't enroll this group, limit of credits per semester will be exceeded. ");
        }

        return Result.success();
    }

    private Result<Void> validateNotDuplicatedEnrollment(String groupKey, List<GroupEnrollment> groupEnrollments) {

        Optional<GroupEnrollment> optionalGroupEnrollment = groupEnrollments.stream()
                .filter(groupEnrollment -> groupEnrollment.getGroupKey().equals(groupKey))
                .findAny();

        if (optionalGroupEnrollment.isPresent()) {
            return Result.error("Enrollment Already Created");
        }

        return Result.success();
    }

    private Result<Void> validateSubject(EnrollmentRelationshipDTO enrollmentRelationshipDTO) {
        if (enrollmentRelationshipDTO.getObligatorySubjectDTO() != null && enrollmentRelationshipDTO.getElectiveSubjectDTO() == null) {
            return validateOrdinarySubject(enrollmentRelationshipDTO.getObligatorySubjectDTO(), enrollmentRelationshipDTO.getStudentDTO());
        } else if (enrollmentRelationshipDTO.getObligatorySubjectDTO() == null && enrollmentRelationshipDTO.getElectiveSubjectDTO() != null) {
            return validateElectiveSubject(enrollmentRelationshipDTO.getElectiveSubjectDTO(), enrollmentRelationshipDTO.getStudentDTO(), enrollmentRelationshipDTO.getStudentGrades());
        } else {
            return Result.success();
        }
    }

    private Result<Void> validateOrdinarySubject(ObligatorySubjectDTO ordinarySubjectDTO, StudentDTO studentDTO ) {
        int semestersCompleted = studentDTO.getSemestersCompleted();
        int subjectSemesterNumber = ordinarySubjectDTO.getSemester();

        if (isSubjectInCurrentSemester(semestersCompleted, subjectSemesterNumber)
                || isSubjectDelayed(semestersCompleted, subjectSemesterNumber)) {
            return Result.success();
        }

        if (isSubjectTooFarAhead(semestersCompleted, subjectSemesterNumber)) {
            return Result.error("You cannot enroll in subjects that are more than two semesters " +
                    "ahead of the semester you are currently taking.");
        }

        return Result.success();
    }

    private Result<Void> validateElectiveSubject(ElectiveSubjectDTO electiveSubjectDTO, StudentDTO studentDTO, List<GradeDTO> studentGrades) {
        int semestersCompleted = studentDTO.getSemestersCompleted();
        Long professionalLineId = studentDTO.getProfessionalLineId();
        ProfessionalLineModality professionalLineModality = studentDTO.getProfessionalLineModality();

        if (!isStudentEligibleForElectives(semestersCompleted)) {
            return Result.error("Only students in 6th semester or above can enroll in elective subjects.");
        }

        if (professionalLineId == null) {
            return Result.error("You must select a professional line before enrolling in elective subjects.");
        }

        if (professionalLineModality == null) {
            return Result.error("You must choose a professional line modality before enrolling in elective subjects.");
        }

        return switch (professionalLineModality) {
            case ELECTIVES -> validateElectivesModality(studentDTO, electiveSubjectDTO, studentGrades);
            case PROFESSIONAL_PRACTICES -> validateProfessionalPracticesModality(studentDTO, electiveSubjectDTO, studentGrades);
        };
    }

    private Result<Void> validateElectivesModality(StudentDTO studentDTO, ElectiveSubjectDTO electiveSubjectDTO, List<GradeDTO> studentGrades) {
        Map<String, List<GradeDTO>> classifiedGrades = classifyElectiveGrades(studentGrades, studentDTO.getProfessionalLineId());

        List<GradeDTO> gradesFromStudentProfessionalLine = classifiedGrades.get("professionalLine");
        List<GradeDTO> gradesFromOtherProfessionalLines = classifiedGrades.get("freeElectives");
        int totalElectives = gradesFromStudentProfessionalLine.size() + gradesFromOtherProfessionalLines.size();

        if (totalElectives >= 8) {
            return Result.error(ERROR_ELECTIVES_COMPLETED);
        }

        Long studentProfessionalLineId = studentDTO.getProfessionalLineId();
        Long electiveSubjectProfessionalLineId = electiveSubjectDTO.getProfessionalLineId();

        if (studentProfessionalLineId.equals(electiveSubjectProfessionalLineId)) {
            return Result.success();
        }

        if (gradesFromOtherProfessionalLines.size() >= 4) {
            return Result.error(ERROR_FREE_ELECTIVES_LIMIT);
        }

        return Result.success();
    }

    private Result<Void> validateProfessionalPracticesModality(StudentDTO studentDTO, ElectiveSubjectDTO electiveSubjectDTO, List<GradeDTO> studentGrades) {
        if (!Objects.equals(electiveSubjectDTO.getProfessionalLineId(), studentDTO.getProfessionalLineId())) {
            return Result.error("You can only enroll in elective subjects from your professional line.");
        }

        List<GradeDTO> studentElectiveGrades = studentGrades.stream()
                .filter(gradeDTO -> gradeDTO.getElectiveSubjectId() != null)
                .toList();

        if (studentElectiveGrades.size() >= 4) {
            return Result.error("You have already completed all your elective subjects " +
                    "for professional practices.");
        }

        return Result.success();
    }

    private boolean isStudentEligibleForElectives(int semestersCompleted) {
        return semestersCompleted >= 6;
    }

    private boolean isSubjectInCurrentSemester(int semestersCompleted, int subjectSemesterNumber) {
        return semestersCompleted == subjectSemesterNumber;
    }

    private boolean isSubjectDelayed(int semestersCompleted, int subjectSemesterNumber) {
        return semestersCompleted > subjectSemesterNumber;
    }

    private boolean isSubjectTooFarAhead(int semestersCompleted, int subjectSemesterNumber) {
        return (subjectSemesterNumber - semestersCompleted) > 2;
    }

    private Map<String, List<GradeDTO>> classifyElectiveGrades(List<GradeDTO> studentGrades, Long professionalLineId) {
        List<GradeDTO> professionalLineGrades = studentGrades.stream()
                .filter(gradeDTO -> gradeDTO.getElectiveSubjectId() != null && gradeDTO.getProfessionalLineId().equals(professionalLineId))
                .toList();

        List<GradeDTO> freeElectiveGrades = studentGrades.stream()
                .filter(gradeDTO -> gradeDTO.getElectiveSubjectId() != null && !gradeDTO.getProfessionalLineId().equals(professionalLineId))
                .toList();

        return Map.of(
                "professionalLine", professionalLineGrades,
                "freeElectives", freeElectiveGrades
        );
    }
}
