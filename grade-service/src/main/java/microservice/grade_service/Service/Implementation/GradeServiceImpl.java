package microservice.grade_service.Service.Implementation;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.SemesterData;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Mappers.GradeMapper;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository,
                                 GradeMapper gradeMapper) {
        this.gradeRepository = gradeRepository;
        this.gradeMapper = gradeMapper;
    }

    @Override
    @Cacheable(value = "gradeById", key = "#gradeId")
    public Result<GradeDTO> getGradeById(Long gradeId) {
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
        return optionalGrade
                .map(grade -> Result.success(gradeMapper.entityToDTO(grade)))
                .orElseGet(() -> Result.error("Grade not found"));
    }


    @Override
    public void initGrade(GradeInsertDTO gradeInsertDTO) {
        Grade grade = gradeMapper.insertDtoToEntity(gradeInsertDTO);
        grade.setStatusAsPending();
        grade.setSchoolPeriod(SemesterData.getCurrentSchoolPeriod());

        gradeRepository.save(grade);
    }

    @Override
    public void deleteGradeById(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new NotFoundException("Grade with ID " + gradeId + " not found"));

        grade.setAsDeleted();
        gradeRepository.save(grade);
    }

    @Override
    public List<GradeDTO> getAllGradeByStudentAccountNumber(String accountNumber) {
        List<Grade> currentGrades = gradeRepository.findByStudentAccountNumber(accountNumber);
        return currentGrades.stream()
                .map(gradeMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<GradeDTO> getGradesByStudentAccountNumber(String accountNumber, String schoolPeriod) {
        List<Grade> grades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, schoolPeriod);
        return grades.stream()
                .map(gradeMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<GradeDTO> getAnnuallyGradesByStudentAccountNumber(String accountNumber) {
        List<Grade> currentGrades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, SemesterData.getCurrentSchoolPeriod());
        List<Grade> oneSemesterBehindGrades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, SemesterData.getBehindSchoolPeriod());

        return Stream.concat(oneSemesterBehindGrades.stream(), currentGrades.stream())
                .sorted(Comparator.comparing(Grade::getSchoolPeriod))
                .map(gradeMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<GradeDTO> getCurrentGradesByStudentAccountNumber(String accountNumber) {
        List<Grade> currentGrades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, SemesterData.getCurrentSchoolPeriod());
        return currentGrades.stream()
                .map(gradeMapper::entityToDTO)
                .toList();
    }

}
