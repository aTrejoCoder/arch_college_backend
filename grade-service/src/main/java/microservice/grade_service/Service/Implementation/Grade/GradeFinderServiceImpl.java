package microservice.grade_service.Service.Implementation.Grade;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.Utils.Grades.GradeStatus;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Schedule.AcademicData;
import microservice.grade_service.Mappers.GradeMapper;
import microservice.grade_service.Model.Grade;
import microservice.grade_service.Repository.GradeRepository;
import microservice.grade_service.Service.GradeFinderService;
import microservice.grade_service.Utils.Credits.GradeFinderFilter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeFinderServiceImpl implements GradeFinderService {

    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;

    @Override
    @Cacheable(value = "gradeById", key = "#gradeId")
    public Result<GradeDTO> getGradeById(Long gradeId) {
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
        return optionalGrade
                .map(grade -> Result.success(gradeMapper.entityToDTO(grade)))
                .orElseGet(() -> Result.error("Grade not found"));
    }

    @Override
    public Page<GradeDTO> getPendingValidationGrades(Pageable pageable) {
        Page<Grade> gradePage = gradeRepository.findByGradeStatus(GradeStatus.PENDING_VALIDATION, pageable);

        return gradePage.map(gradeMapper::entityToDTO);
    }

    @Override
    public Page<GradeDTO> getGradesByFilters(GradeFinderFilter gradeFilter, Pageable pageable) {
        Specification<Grade> specification = buildGradeSpecification(gradeFilter);
        Page<Grade> gradePage = gradeRepository.findAll(specification, pageable);
        return gradePage.map(gradeMapper::entityToDTO);
    }

    @Override
    public List<GradeDTO> getAnnuallyGradesByStudentAccountNumber(String accountNumber) {
        List<Grade> currentGrades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, AcademicData.getCurrentSchoolPeriod());
        List<Grade> oneSemesterBehindGrades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, AcademicData.getBehindSchoolPeriod());

        return Stream.concat(oneSemesterBehindGrades.stream(), currentGrades.stream())
                .sorted(Comparator.comparing(Grade::getSchoolPeriod))
                .map(gradeMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<GradeDTO> getCurrentGradesByStudentAccountNumber(String accountNumber) {
        List<Grade> currentGrades = gradeRepository.findByStudentAccountNumberAndSchoolPeriod(accountNumber, AcademicData.getCurrentSchoolPeriod());
        return currentGrades.stream()
                .map(gradeMapper::entityToDTO)
                .toList();
    }

    private Specification<Grade> buildGradeSpecification(GradeFinderFilter gradeFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (gradeFilter.getAccountNumber() != null && !gradeFilter.getAccountNumber().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("studentAccountNumber"), gradeFilter.getAccountNumber()));
            }

            if (gradeFilter.getSchoolPeriod() != null && !gradeFilter.getSchoolPeriod().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("schoolPeriod"), gradeFilter.getSchoolPeriod()));
            }

            if (gradeFilter.getSubjectId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("subjectId"), gradeFilter.getSubjectId()));
            }

            if (gradeFilter.getSubjectType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("subjectType"), gradeFilter.getSubjectType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
