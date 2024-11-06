package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectDTO;
import microservice.subject_service.DTOs.Subject.OrdinarySubjectInsertDTO;
import microservice.subject_service.Service.OrdinarySubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/subjects/ordinaries")
public class OrdinarySubjectController {

    private final OrdinarySubjectService ordinarySubjectService;

    @Autowired
    public OrdinarySubjectController(OrdinarySubjectService ordinarySubjectService) {
        this.ordinarySubjectService = ordinarySubjectService;
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<OrdinarySubjectDTO>> getOrdinarySubjectById(@PathVariable Long subjectId) {
        Result<OrdinarySubjectDTO> areaResult = ordinarySubjectService.getSubjectById(subjectId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Ordinary Subject", "ID", subjectId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "ID", subjectId, areaResult.getData()));
    }

    @GetMapping("/by-semester/{semester}")
    public ResponseEntity<ResponseWrapper<Page<OrdinarySubjectDTO>>> getOrdinarySubjectById(@PathVariable int semester,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdinarySubjectDTO> ordinarySubjectDTOS = ordinarySubjectService.getSubjectBySemester(semester, pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "Semester", semester, ordinarySubjectDTOS));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<OrdinarySubjectDTO>> getOrdinarySubjectByName(@PathVariable String name) {
        Result<OrdinarySubjectDTO> areaResult = ordinarySubjectService.getSubjectByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("OrdinarySubject", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subject", "name", name, areaResult.getData()));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<Page<OrdinarySubjectDTO>>> getAllOrdinarySubjects(@RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdinarySubjectDTO> professionalLines = ordinarySubjectService.getAllSubjects(pageable);

        return ResponseEntity.ok(ResponseWrapper.found("Ordinary Subjects", professionalLines));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createOrdinarySubject(@Valid @RequestBody OrdinarySubjectInsertDTO areaInsertDTO) {
        ordinarySubjectService.createOrdinarySubject(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Ordinary Subject"));
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> updateOrdinarySubject(@Valid @RequestBody OrdinarySubjectInsertDTO areaInsertDTO,
                                                                       @PathVariable Long subjectId) {
        ordinarySubjectService.updateOrdinarySubject(areaInsertDTO, subjectId);

        return ResponseEntity.ok(ResponseWrapper.updated(null,"Ordinary Subject"));
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteOrdinarySubjectById(@PathVariable Long subjectId) {
        ordinarySubjectService.deleteOrdinarySubject(subjectId);

        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Ordinary Subject"));
    }
}
