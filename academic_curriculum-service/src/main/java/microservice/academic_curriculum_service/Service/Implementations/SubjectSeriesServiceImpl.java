package microservice.academic_curriculum_service.Service.Implementations;

import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import microservice.common_classes.DTOs.Subject.SubjectSeriesDTO;
import microservice.academic_curriculum_service.DTOs.SocialNetwork.SubjectSeriesInsertDTO;
import microservice.academic_curriculum_service.Mappers.SubjectSeriesMapper;
import microservice.academic_curriculum_service.Model.Subject.ElectiveSubject;
import microservice.academic_curriculum_service.Model.Subject.ObligatorySubject;
import microservice.academic_curriculum_service.Model.Subject.Subject;
import microservice.academic_curriculum_service.Model.Subject.SubjectSeries;
import microservice.academic_curriculum_service.Repository.ElectiveSubjectRepository;
import microservice.academic_curriculum_service.Repository.ObligatorySubjectRepository;
import microservice.academic_curriculum_service.Repository.SubjectSeriesRepository;
import microservice.academic_curriculum_service.Service.SubjectSeriesService;

import microservice.academic_curriculum_service.Service.SubjectService;
import microservice.common_classes.DTOs.Subject.*;
import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.SubjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubjectSeriesServiceImpl implements SubjectSeriesService {

    private final ObligatorySubjectRepository obligatorySubjectRepository;
    private final SubjectSeriesRepository subjectSeriesRepository;
    private final SubjectService<ObligatorySubjectDTO, ObligatorySubjectInsertDTO> obligatorySubjectService;
    private final SubjectService<ElectiveSubjectDTO, ElectiveSubjectInsertDTO> electiveSubjectService;
    private final ElectiveSubjectRepository electiveSubjectRepository;
    private final SubjectSeriesMapper subjectSeriesMapper;

    @Override
    public Result<Void> validateSubjectSeriesCreation(SubjectSeriesInsertDTO subjectSeriesInsertDTO) {
        SubjectType subjectType = subjectSeriesInsertDTO.getSubjectType();
        Set<Long> providedIds = subjectSeriesInsertDTO.getSubjectsIds();

        if (subjectType == SubjectType.OBLIGATORY) {
            return validateSubjects(providedIds, obligatorySubjectService);
        } else if (subjectType == SubjectType.ELECTIVE) {
            return validateSubjects(providedIds, electiveSubjectService);
        } else {
            throw new IllegalArgumentException("Unsupported SubjectType: " + subjectType);
        }
    }

    private <T extends SubjectDTO> Result<Void> validateSubjects(
            Set<Long> subjectIds,
            SubjectService<T, ?> subjectService
    ) {
        Result<List<T>> subjectResult = subjectService.getSubjectByIdsIn(subjectIds);

        if (!subjectResult.isSuccess()) {
            return Result.error(subjectResult.getErrorMessage());
        }

        List<T> subjectDTOS = subjectResult.getData();
        for (var subject : subjectDTOS) {
            if (subject.getSeriesId() != null) {
                return Result.error("Subject With Key " + subject.getKey() + " already has serialization");
            }
        }

        return Result.success();
    }

    @Override
    @Transactional
    public SubjectSeriesDTO createSubjectSeries(SubjectSeriesInsertDTO subjectSeriesInsertDTO) {
        SubjectSeries subjectSeries = subjectSeriesMapper.insertDtoToEntity(subjectSeriesInsertDTO);

        subjectSeriesRepository.saveAndFlush(subjectSeries);

        addSeriesToSubjects(subjectSeriesInsertDTO, subjectSeries);

        return subjectSeriesMapper.entityToDTO(subjectSeries);
    }

    @Override
    @Cacheable(value = "subjectSeriesByObligatorySubjectId", key = "#obligatorySubjectId")
    public SubjectSeriesDTO getSubjectSeriesByObligatorySubjectId(Long obligatorySubjectId) {
        ObligatorySubject obligatorySubject = obligatorySubjectRepository.findById(obligatorySubjectId)
                .orElseThrow(() -> new EntityNotFoundException("Obligatory Subject With Id " + obligatorySubjectId + " Not Found"));

        if (obligatorySubject.getSeries() == null) {
            return null;
        }

        SubjectSeries subjectSeries = obligatorySubject.getSeries();

        return subjectSeriesMapper.entityToDTO(subjectSeries);
    }

    @Override
    @Cacheable(value = "subjectSeriesByElectiveSubjectId", key = "#electiveSubjectId")
    public SubjectSeriesDTO getSubjectSeriesByElectiveSubjectId(Long electiveSubjectId) {
        ElectiveSubject electiveSubject = electiveSubjectRepository.findById(electiveSubjectId)
                .orElseThrow(() -> new EntityNotFoundException("Elective Subject With Id " + electiveSubjectId + " Not Found"));

        if (electiveSubject.getSeries() == null) {
            return null;
        }

        SubjectSeries subjectSeries = electiveSubject.getSeries();

        return subjectSeriesMapper.entityToDTO(subjectSeries);
    }

    @Override
    @Cacheable(value = "allSubjectSeries", key = "#pageable")
    public Page<SubjectSeriesDTO> getAll(Pageable pageable) {
        Page<SubjectSeries> subjectSeriesPage = subjectSeriesRepository.findAll(pageable);
        return subjectSeriesPage.map(subjectSeriesMapper::entityToDTO);
    }

    @Override
    public void deleteSubjectSeriesById(Long subjectSeriesId) {
        if (!subjectSeriesRepository.existsById(subjectSeriesId)) {
            throw new EntityNotFoundException("Subject Series With Id "+ subjectSeriesId + " Not Found");
        }
        subjectSeriesRepository.deleteById(subjectSeriesId);
    }

    private void addSeriesToSubjects(SubjectSeriesInsertDTO subjectSeriesInsertDTO, SubjectSeries subjectSeries) {
        Set<Long> subjectsIds = subjectSeriesInsertDTO.getSubjectsIds();

        List<? extends Subject> subjectsToAdd = fetchSubjectsByType(subjectSeriesInsertDTO.getSubjectType(), subjectsIds);

        addSubjectsToSeries(subjectSeries, subjectsToAdd);
    }

    private void addSubjectsToSeries(SubjectSeries subjectSeries, List<? extends Subject> subjectsToAdd) {
        if (!subjectsToAdd.isEmpty()) {
            if (subjectsToAdd.get(0) instanceof ObligatorySubject) {
                for (ObligatorySubject subject : (List<ObligatorySubject>) subjectsToAdd) {
                    subjectSeries.addObligatorySubject(subject);
                }
            } else if (subjectsToAdd.get(0) instanceof ElectiveSubject) {
                for (ElectiveSubject subject : (List<ElectiveSubject>) subjectsToAdd) {
                    subjectSeries.addElectiveSubject(subject);
                }
            }
        }
        subjectSeriesRepository.saveAndFlush(subjectSeries);
    }

    private List<? extends Subject> fetchSubjectsByType(SubjectType subjectType, Set<Long> subjectsIds) {
        if (subjectType == SubjectType.OBLIGATORY) {
            return obligatorySubjectRepository.findByIdIn(subjectsIds);
        } else if (subjectType == SubjectType.ELECTIVE) {
            return electiveSubjectRepository.findByIdIn(subjectsIds);
        }
        throw new IllegalArgumentException("Unsupported SubjectType: " + subjectType);
    }
}
