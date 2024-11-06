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

@RestController
@RequestMapping("/v1/api/careers")
public class CareerController {

    private final CareerService careerService;

    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping("/{careerId}")
    public ResponseEntity<ResponseWrapper<CareerDTO>> getCareerById(@PathVariable Long careerId) {
        Result<CareerDTO> careerResult = careerService.getCareerById(careerId);
        if (!careerResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound( "Career", "ID", careerId));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Career", "ID", careerId, careerResult.getData()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseWrapper<CareerDTO>> getCareerByName(@PathVariable String name) {
        Result<CareerDTO> careerResult = careerService.getCareerByName(name);
        if (!careerResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Career", "name", name));
        }

        return ResponseEntity.ok(ResponseWrapper.found("Career", "name", name, careerResult.getData()));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<CareerDTO>>> getAllCareers() {
        List<CareerDTO> career = careerService.getAllCareers();

        return ResponseEntity.ok(ResponseWrapper.found("Careers", career));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createCareer(@Valid @RequestBody CareerInsertDTO careerInsertDTO) {
        careerService.createCareer(careerInsertDTO);

        return ResponseEntity.ok(ResponseWrapper.created(null,"Career"));
    }

    @PutMapping("/{careerId}")
    public ResponseEntity<ResponseWrapper<Void>> updateCareer(@Valid @RequestBody CareerInsertDTO careerInsertDTO,
                                                            @PathVariable Long careerId) {
        careerService.updateCareer(careerInsertDTO, careerId);

        return ResponseEntity.ok(ResponseWrapper.updated(null,"Career"));
    }
}
