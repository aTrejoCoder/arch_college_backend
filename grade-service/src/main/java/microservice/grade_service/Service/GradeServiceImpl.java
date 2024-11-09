package microservice.grade_service.Service;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.Utils.Result;
import microservice.grade_service.DTOs.GradeDTO;
import microservice.grade_service.DTOs.GradeInsertDTO;
import microservice.grade_service.Mappers.GradeMapper;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

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
    public Result<Void> authorizeGradeById(Long gradeId) {
    Grade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new NotFoundException("Grade with ID " + gradeId + " not found"));;

    if (grade.isAuthorized()) {
        return Result.error("Grade Already Authorized");
    }

    grade.removePendingStatus();
    grade.setAsAuthorized();
    gradeRepository.save(grade);

    return Result.success();
    }

    @Override
    public Result<Void> validateGradeCreation(GradeInsertDTO gradeInsertDTO) {
        List<Grade> grades = (gradeInsertDTO.getOrdinarySubjectId() != null)
                ? gradeRepository.findByStudentIdAndOrdinarySubjectId(gradeInsertDTO.getStudentId(), gradeInsertDTO.getOrdinarySubjectId())
                : gradeRepository.findByStudentIdAndElectiveSubjectId(gradeInsertDTO.getStudentId(), gradeInsertDTO.getElectiveSubjectId());

        if (grades.isEmpty()) {
            return Result.success(); // No subject cursed yet, grade can be created
        }

        int notApprovedCount = 0;

        for (Grade grade : grades) {
            // Look if grade is already inited
            if (grade.isPending()) {
                return Result.error("Grade for this subject is already being processes, wait until validation");
            }

            // Look if is already approved
            if (grade.isGradeApproved()) {
                return Result.error("Grade is already approved and cannot be modified.");
            } else {
                notApprovedCount++;
            }
        }

        // Validate if the student has failed the grade more than twice
        if (notApprovedCount >= 3) {
            return Result.error("Grade failed more than twice; it can only be recorded through an extraordinary process.");
        }

        return Result.success();
    }


    @Override
    public void initGrade(GradeInsertDTO gradeInsertDTO) {
        Grade grade = gradeMapper.insertDtoToEntity(gradeInsertDTO);
        grade.setStatusAsPending();
        grade.setSchoolPeriod("2024");

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
    public Result<Void> validateGradeRelationships(GradeInsertDTO gradeInsertDTO) {
        return Result.success();
    }

    @Override
    public List<GradeDTO> getAllGradeByStudent(Long studentId) {
        List<Grade> currentGrades = gradeRepository.findByStudentId(studentId);
        return currentGrades.stream()
                .map(gradeMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<GradeDTO> getGradesByStudentAndSchoolPeriod(Long studentId, String schoolPeriod) {
        List<Grade> currentGrades = gradeRepository.findByStudentIdAndSchoolPeriod(studentId, schoolPeriod);
        return currentGrades.stream()
                .map(gradeMapper::entityToDTO)
                .toList();
    }
}
