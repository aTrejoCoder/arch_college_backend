package microservice.grade_service.Service.Implementation;

import jakarta.persistence.EntityNotFoundException;
import microservice.common_classes.DTOs.Carrer.CareerDTO;
import microservice.common_classes.DTOs.Student.StudentExtendedDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.Utils.Result;
import microservice.grade_service.Model.AcademicHistory;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Model.GradeNamed;
import microservice.grade_service.Repository.AcademicHistoryRepository;
import microservice.grade_service.Service.AcademicHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AcademicHistoryServiceImpl implements AcademicHistoryService {

    private final AcademicHistoryRepository academicHistoryRepository;

    @Autowired
    public AcademicHistoryServiceImpl(AcademicHistoryRepository academicHistoryRepository) {
        this.academicHistoryRepository = academicHistoryRepository;
    }

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
    public void initAcademicHistory(StudentExtendedDTO studentExtendedDTO, CareerDTO careerDTO) {

        AcademicHistory academicHistory = AcademicHistory.builder()
                .careerKey(careerDTO.getKey())
                .careerName(careerDTO.getName())
                .studentAccountNumber(studentExtendedDTO.getAccountNumber())
                .incomeGeneration(studentExtendedDTO.getIncomeGeneration())
                .studentName(studentExtendedDTO.getFirstName() + " " + studentExtendedDTO.getLastName())
                .academicAverAge(0.00)
                .build();

        academicHistory.initCreditAdvance(careerDTO.getObligatoryCredits(), careerDTO.getElectiveCredits());

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

    private GradeNamed mapGradeToGradesNamed(Grade grade) {
        return new GradeNamed();
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
        GradeNamed gradeNamed = mapGradeToGradesNamed(grade);

        studentGrades.add(gradeNamed);

        sortGradeNameList(studentGrades);
    }
}
