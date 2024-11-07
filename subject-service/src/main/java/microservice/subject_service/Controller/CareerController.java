package microservice.subject_service.Controller;

import jakarta.validation.Valid;
import microservice.common_classes.Utils.ResponseWrapper;
import microservice.common_classes.Utils.Result;
import microservice.subject_service.DTOs.Career.CareerDTO;
import microservice.subject_service.DTOs.Career.CareerInsertDTO;
import microservice.subject_service.Service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/api/careers")
public class CareerController {

    private final CareerService careerService;

    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @Operation(summary = "Get Career by ID", description = "Fetches the career with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Career found", content = @Content(schema = @Schema(implementation = CareerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Career not found", content = @Content)
    })
    @GetMapping("/{careerId}")
    public ResponseEntity<ResponseWrapper<CareerDTO>> getCareerById(@PathVariable Long careerId) {
        Result<CareerDTO> careerResult = careerService.getCareerById(careerId);
        if (!careerResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Career", "ID", careerId));
        }
        return ResponseEntity.ok(ResponseWrapper.found("Career", "ID", careerId, careerResult.getData()));
    }

    @Operation(summary = "Get Career by Name", description = "Fetches the career with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Career found", content = @Content(schema = @Schema(implementation = CareerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Career not found", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<CareerDTO>> getCareerByName(@PathVariable String name) {
        Result<CareerDTO> careerResult = careerService.getCareerByName(name);
        if (!careerResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Career", "name", name));
        }
        return ResponseEntity.ok(ResponseWrapper.found("Career", "name", name, careerResult.getData()));
    }

    @Operation(summary = "Get All Careers", description = "Fetches all available careers")
    @ApiResponse(responseCode = "200", description = "Careers found", content = @Content(schema = @Schema(implementation = CareerDTO.class)))
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<CareerDTO>>> getAllCareers() {
        List<CareerDTO> career = careerService.getAllCareers();
        return ResponseEntity.ok(ResponseWrapper.found("Careers", career));
    }

    @Operation(summary = "Create Career", description = "Creates a new career entry")
    @ApiResponse(responseCode = "201", description = "Career created", content = @Content)
    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createCareer(@Valid @RequestBody CareerInsertDTO careerInsertDTO) {
        careerService.createCareer(careerInsertDTO);
        return ResponseEntity.ok(ResponseWrapper.created(null, "Career"));
    }

    @Operation(summary = "Update Career", description = "Updates an existing career by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Career updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Career not found", content = @Content)
    })
    @PutMapping("/{careerId}")
    public ResponseEntity<ResponseWrapper<Void>> updateCareer(
            @Valid @RequestBody CareerInsertDTO careerInsertDTO,
            @Parameter(description = "ID of the career to be updated") @PathVariable Long careerId) {
        careerService.updateCareer(careerInsertDTO, careerId);
        return ResponseEntity.ok(ResponseWrapper.updated(null, "Career"));
    }
}
