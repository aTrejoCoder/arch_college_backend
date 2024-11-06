package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.common_classes.Utils.StatusType;

import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineDTO;
import microservice.subject_service.DTOs.ProfessionalLine.ProfessionalLineInsertDTO;
import microservice.subject_service.Service.ProfessionalLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/professional_lines")
public class ProfessionalLineController {

    private final ProfessionalLineService areaService;

    @Autowired
    public ProfessionalLineController(ProfessionalLineService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<ProfessionalLineDTO>> getProfessionalLineById(@PathVariable Long professionalLineId) {
        Result<ProfessionalLineDTO> areaResult = areaService.getProfessionalLineById(professionalLineId);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Professional Line", "ID", professionalLineId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("ProfessionalLine", "ID", professionalLineId, areaResult.getData()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<ProfessionalLineDTO>> getProfessionalLineByName(@PathVariable String name) {
        Result<ProfessionalLineDTO> areaResult = areaService.getProfessionalLineByName(name);
        if (!areaResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("ProfessionalLine", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Professional Line", "name", name, areaResult.getData()));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<ProfessionalLineDTO>>> getAllProfessionalLines() {
        List<ProfessionalLineDTO> professionalLines = areaService.getAllProfessionalLines();

        return ResponseEntity.ok(ResponseWrapper.found("Professional Lines", professionalLines));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createProfessionalLine(@Valid @RequestBody ProfessionalLineInsertDTO areaInsertDTO) {
        areaService.createProfessionalLine(areaInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Professional Line"));
    }

    @PutMapping("/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Void>> updateProfessionalLine(@Valid @RequestBody ProfessionalLineInsertDTO areaInsertDTO,
                                                            @PathVariable Long professionalLineId) {
        areaService.updateProfessionalLineName(areaInsertDTO, professionalLineId);

        return ResponseEntity.ok(ResponseWrapper.updated(null,"Professional Line"));
    }

    @DeleteMapping("/{professionalLineId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteProfessionalLineById(@PathVariable Long professionalLineId) {
        areaService.deleteProfessionalLine(professionalLineId);

        return ResponseEntity.ok(ResponseWrapper.deleted(null,"Professional Line"));
    }
}
