package microservice.grade_service.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.grade_service.Mappers.GradeMapper;
import microservice.grade_service.Model.AcademicHistory;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.GradeNamed;
import microservice.grade_service.Repository.AcademicHistoryRepository;
import microservice.grade_service.Service.AcademicHistoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AcademicHistoryServiceImpl implements AcademicHistoryService {

    private final AcademicHistoryRepository academicHistoryRepository;
    private final GradeMapper gradeMapper;

    public Result<Void> validateAcademicHistoryInit(String accountNumber) {
       Optional<AcademicHistory> optionalAcademicHistory = academicHistoryRepository.findByStudentAccountNumber(accountNumber);
        if (optionalAcademicHistory.isPresent()) {
            return Result.error("student with account number" + accountNumber + "already have an academic history");
        }

        return Result.success();
    }

    public void setNewGradeToAcademicHistory(AcademicHistory academicHistory, Grade grade, OrdinarySubjectDTO ordinarySubjectDTO) {
        addGrade(grade,academicHistory.getGrades());
        updateCreditData(academicHistory, ordinarySubjectDTO.getCredits());

        academicHistoryRepository.save(academicHistory);
    }

    public void addSpecialityToAcademicHistory(String speciality, AcademicHistory academicHistory) {
        academicHistory.setSpeciality(speciality);
        academicHistoryRepository.save(academicHistory);
    }


    @Override
    public void initAcademicHistory(StudentDTO studentDTO, CareerDTO careerDTO) {

        AcademicHistory academicHistory = AcademicHistory.builder()
                .careerKey(careerDTO.getKey())
                .careerName(careerDTO.getName())
                .studentAccountNumber(studentDTO.getAccountNumber())
                .incomeGeneration(studentDTO.getIncomeGeneration())
                .studentName(studentDTO.getFirstName() + " " + studentDTO.getLastName())
                .academicAverAge(0.00)
                .build();

        academicHistory.initCreditAdvance(careerDTO.getTotalObligatoryCredits(), careerDTO.getTotalElectiveCredits());

        academicHistoryRepository.save(academicHistory);
    }

    @Override
    public AcademicHistory getAcademicHistoryByAccountNumber(String accountNumber) {
        Optional<AcademicHistory> optionalAcademicHistory = academicHistoryRepository.findByStudentAccountNumber(accountNumber);
        if (optionalAcademicHistory.isEmpty()) {
            throw new EntityNotFoundException("student with account number" + accountNumber + "doesn't have academic history");
        }

        return optionalAcademicHistory.get();
    }

    private void sortGradeNameList(List<GradeNamed> grades) {
        grades.sort(Comparator.comparingInt(GradeNamed::getSubjectKey));
    }

    private void updateCreditData(AcademicHistory academicHistory, int credits) {
        academicHistory.addOrdinaryCreditAdvance(credits);
        academicHistory.reCalculatePercentages();
        academicHistory.reCalculateAverage();
    }

    private void addGrade(Grade grade, List<GradeNamed> studentGrades) {
        GradeNamed gradeNamed = gradeMapper.entityToNamedDTO(grade);

        studentGrades.add(gradeNamed);

        sortGradeNameList(studentGrades);
    }

    @Override
    public void validateUniqueAcademicHistoryPerStudent(String accountNumber) {
        Optional<AcademicHistory> optionalAcademicHistory = academicHistoryRepository.findByStudentAccountNumber(accountNumber);
        if (optionalAcademicHistory.isPresent()) {
            throw new RuntimeException("Student with account number (" + accountNumber + ") already has academic history");
        }
    }
}
