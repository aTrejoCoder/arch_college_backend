package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectDTO;
import microservice.subject_service.DTOs.Subject.ElectiveSubjectInsertDTO;
import microservice.subject_service.Service.ElectiveSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/subjects/electives")
public class ElectiveSubjectController {

    private final ElectiveSubjectService electiveSubjectService;

    @Autowired
    public ElectiveSubjectController(ElectiveSubjectService electiveSubjectService) {
        this.electiveSubjectService = electiveSubjectService;
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectById(@PathVariable Long subjectId) {
        Result<ElectiveSubjectDTO> areaResult = electiveSubjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Elective Subject", "ID", subjectId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "ID", subjectId, areaResult.getData()));
    }

    @GetMapping("by-professional_line/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getElectiveSubjectByProfessionalLineId(@PathVariable Long professionalLineId,
                                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ElectiveSubjectDTO> electiveSubjectDTOS = electiveSubjectService.getSubjectByProfessionalLineId(professionalLineId, pageable);
        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "Professional Line ID", professionalLineId, electiveSubjectDTOS));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ElectiveSubjectDTO>> getElectiveSubjectByName(@PathVariable String name) {
        Result<ElectiveSubjectDTO> areaResult = electiveSubjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("ElectiveSubject", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Elective Subject", "name", name, areaResult.getData()));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<ElectiveSubjectDTO>>> getAllElectiveSubjects(@RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElectiveSubjectDTO> professionalLines = electiveSubjectService.getSubjectAll(pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Elective Subjects", professionalLines));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO areaInsertDTO) {
        electiveSubjectService.createElectiveSubject(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Elective Subject"));
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateElectiveSubject(@Valid @RequestBody ElectiveSubjectInsertDTO areaInsertDTO,
                                                                       @PathVariable Long subjectId) {
        electiveSubjectService.updateElectiveSubject(areaInsertDTO, subjectId);

        return ResponseEntity.ok(ResponseWrapper.updated(null,"Elective Subject"));
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteElectiveSubjectById(@PathVariable Long subjectId) {
        electiveSubjectService.deleteElectiveSubject(subjectId);

        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Elective Subject"));
    }
}
