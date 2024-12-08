package microservice.enrollment_service.Controller.Preload;

import microservice.enrollment_service.Model.Preload.Group;
import microservice.enrollment_service.Service.PreloadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*
 Using Long Polling to Handle All The Amount External Data
*/

@RestController
@RequestMapping("v1/api/schedules/")
public class SchedulePreLoadController {

    @Autowired
    private PreloadDataService<Group> schedulePreloadDataService;

    @PostMapping("/preload")
    public ResponseEntity<String> preloadSchedules() {
        String processId = UUID.randomUUID().toString();
        schedulePreloadDataService.startPreload(processId);

        return ResponseEntity.ok(processId);
    }

    @GetMapping("/preload/{processId}/status")
    public ResponseEntity<String> getProcessStatus(@PathVariable String processId) {
        String status = schedulePreloadDataService.getPreloadStatus(processId);

        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Process ID not found");
        }

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearSchedules() {
        schedulePreloadDataService.clear();

        return ResponseEntity.ok().build();
    }
}
