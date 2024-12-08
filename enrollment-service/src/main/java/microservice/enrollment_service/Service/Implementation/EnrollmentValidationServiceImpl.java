package microservice.enrollment_service.Service.Implementation;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.Utils.ProfessionalLineModality;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.common_classes.Utils.SubjectType;
import microservice.enrollment_service.DTOs.EnrollmentRelationship;
import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import microservice.enrollment_service.Model.Preload.Grade;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import microservice.enrollment_service.Model.Preload.Student;
import microservice.enrollment_service.Model.Enrollment;
import microservice.enrollment_service.Repository.EnrollmentRepository;
import microservice.enrollment_service.Service.EnrollmentValidationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EnrollmentValidationServiceImpl implements EnrollmentValidationService {
    public final int MAX_CREDITS_PER_SCHOOL_PERIOD = 120;
    private static final String ERROR_ELECTIVES_COMPLETED = "You have already completed all your elective subjects. Enrollment in additional electives is not allowed.";
    private static final String ERROR_FREE_ELECTIVES_LIMIT = "You have reached the limit for free electives from other professional lines. You can only enroll in electives from your professional line.";
    private final EnrollmentRepository enrollmentRepository;
    private final String schoolPeriod = AcademicData.getCurrentSchoolPeriod();

    public EnrollmentValidationServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Result<Void> validateEnrollment(EnrollmentInsertDTO enrollmentInsert, EnrollmentRelationship enrollmentRelationship, String accountNumber) {
        List<Enrollment> groupEnrollments = enrollmentRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, schoolPeriod);

        Result<Void> gradeConflictResult = validateNotGradeConflict(enrollmentRelationship);
        if (!gradeConflictResult.isSuccess()) {
            return Result.error(gradeConflictResult.getErrorMessage());
        }

        Result<Void> notDuplicatedResult = validateNotDuplicatedEnrollment(enrollmentInsert.getGroupKey(), enrollmentInsert.getSubjectKey() ,groupEnrollments);
        if (!notDuplicatedResult.isSuccess()) {
            return Result.error(notDuplicatedResult.getErrorMessage());
        }

        Result<Void> validateSubjectResult = validateSubject(enrollmentRelationship);
        if (!validateSubjectResult.isSuccess()) {
            return Result.error(validateSubjectResult.getErrorMessage());
        }

        Result<Void> maxCreditsResult = validateMaximumCreditsPerStudent(accountNumber, enrollmentRelationship);
        if (!maxCreditsResult.isSuccess()) {
            return Result.error(maxCreditsResult.getErrorMessage());
        }


        return Result.success();
    }

    private Result<Void> validateNotGradeConflict(EnrollmentRelationship enrollmentRelationship) {
         List<Grade> studentGrades = enrollmentRelationship.getStudentGrades();

        if (enrollmentRelationship.getObligatorySubject() != null) {
            List<Grade> gradesPerSubject = studentGrades.stream()
                    .filter(grade -> grade.getSubjectId() != null
                            && grade.getSubjectType() == SubjectType.OBLIGATORY
                            && grade.getSubjectId().equals(enrollmentRelationship.getObligatorySubject().getId()))
                    .toList();

             // Doesn't have grades for this subject, won't have conflicts
             if(gradesPerSubject.isEmpty()) {
                 return Result.success();
             }

             int notApprovedCount = 0;
             for (var grade : gradesPerSubject) {
                 // Not Approved
                 if (!grade.isApproved()) {
                     notApprovedCount++;
                 } else {
                     // Approved
                     return Result.error("Can't make enrollment, subject already approved");
                 }
             }

             if (notApprovedCount >= 2) {
                 return Result.error("This subject has not been passed twice. From now on, it can only be completed through an extraordinary exam.");
             }

             return Result.success();
         }

        return Result.success();
    }

    private Result<Void> validateMaximumCreditsPerStudent(String accountNumber, EnrollmentRelationship enrollmentRelationship) {
        List<Enrollment> currentStudentEnrollments = enrollmentRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, schoolPeriod);
        int currentTotalCredits = 0;

        for (var studentEnrollment : currentStudentEnrollments) {
            currentTotalCredits += studentEnrollment.getSubjectCredits();
        }

        if (currentTotalCredits > MAX_CREDITS_PER_SCHOOL_PERIOD) {
            return Result.error("Can't enroll this group, limit of credits per semester will be exceeded. ");
        }

        return Result.success();
    }

    private Result<Void> validateNotDuplicatedEnrollment(String groupKey, String subjectKey ,List<Enrollment> groupEnrollments) {
        Optional<Enrollment> optionalEnrollment = groupEnrollments.stream()
                .filter(groupEnrollment -> (groupEnrollment.getGroupKey().equals(groupKey) && groupEnrollment.getSubjectKey().equals(groupKey)))
                .findAny();

        if (optionalEnrollment.isPresent()) {
            return Result.error("Enrollment Already Created");
        }

        optionalEnrollment = groupEnrollments.stream()
                .filter(groupEnrollment -> groupEnrollment.getSubjectKey().equals(subjectKey))
                .findAny();

        if (optionalEnrollment.isPresent()) {
            return Result.error("Subject Already Enrolled");
        }

        return Result.success();
    }

    private Result<Void> validateSubject(EnrollmentRelationship enrollmentRelationship) {
        if (enrollmentRelationship.getObligatorySubject() != null && enrollmentRelationship.getElectiveSubject() == null) {
            return validateOrdinarySubject(enrollmentRelationship.getObligatorySubject(), enrollmentRelationship.getStudent());
        } else if (enrollmentRelationship.getObligatorySubject() == null && enrollmentRelationship.getElectiveSubject() != null) {
            return validateElectiveSubject(enrollmentRelationship.getElectiveSubject(), enrollmentRelationship.getStudent(), enrollmentRelationship.getStudentGrades());
        } else {
            return Result.success();
        }
    }

    private Result<Void> validateOrdinarySubject(ObligatorySubject ordinarySubject, Student student ) {
        int semestersCompleted = student.getSemestersCompleted();
        int subjectSemesterNumber = ordinarySubject.getSemester();

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

    private Result<Void> validateElectiveSubject(ElectiveSubject electiveSubject, Student student, List<Grade> studentGrades) {
        int semestersCompleted = student.getSemestersCompleted();
        Long professionalLineId = student.getProfessionalLineId();
        ProfessionalLineModality professionalLineModality = student.getProfessionalLineModality();

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
            case ELECTIVES -> validateElectivesModality(student, electiveSubject, studentGrades);
            case PROFESSIONAL_PRACTICES -> validateProfessionalPracticesModality(student, electiveSubject, studentGrades);
        };
    }

    private Result<Void> validateElectivesModality(Student student, ElectiveSubject electiveSubject, List<Grade> studentGrades) {
        Map<String, List<Grade>> classifiedGrades = classifyElectiveGrades(studentGrades, student.getProfessionalLineId());

        List<Grade> gradesFromStudentProfessionalLine = classifiedGrades.get("professionalLine");
        List<Grade> gradesFromOtherProfessionalLines = classifiedGrades.get("freeElectives");
        int totalElectives = gradesFromStudentProfessionalLine.size() + gradesFromOtherProfessionalLines.size();

        if (totalElectives >= 8) {
            return Result.error(ERROR_ELECTIVES_COMPLETED);
        }

        Long studentProfessionalLineId = student.getProfessionalLineId();
        Long electiveSubjectProfessionalLineId = electiveSubject.getProfessionalLineId();

        if (studentProfessionalLineId.equals(electiveSubjectProfessionalLineId)) {
            return Result.success();
        }

        if (gradesFromOtherProfessionalLines.size() >= 4) {
            return Result.error(ERROR_FREE_ELECTIVES_LIMIT);
        }

        return Result.success();
    }

    private Result<Void> validateProfessionalPracticesModality(Student student, ElectiveSubject electiveSubject, List<Grade> studentGrades) {
        if (!Objects.equals(electiveSubject.getProfessionalLineId(), student.getProfessionalLineId())) {
            return Result.error("You can only enroll in elective subjects from your professional line.");
        }

        List<Grade> studentElectiveGrades = studentGrades.stream()
                .filter(grade -> (grade.getSubjectId() != null && grade.getSubjectType() == SubjectType.ELECTIVE))
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

    private Map<String, List<Grade>> classifyElectiveGrades(List<Grade> studentGrades, Long professionalLineId) {
        List<Grade> professionalLineGrades = studentGrades.stream()
                .filter(grade ->(grade.getSubjectId() != null && grade.getSubjectType() == SubjectType.ELECTIVE) && grade.getProfessionalLineId().equals(professionalLineId))
                .toList();

        List<Grade> freeElectiveGrades = studentGrades.stream()
                .filter(grade -> (grade.getSubjectId() != null && grade.getSubjectType() == SubjectType.ELECTIVE)  && !grade.getProfessionalLineId().equals(professionalLineId))
                .toList();


        return Map.of(
                "professionalLine", professionalLineGrades,
                "freeElectives", freeElectiveGrades
        );
    }
}
