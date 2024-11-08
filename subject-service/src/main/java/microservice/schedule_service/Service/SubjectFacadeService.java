package microservice.schedule_service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Dummy Facade
@Service
public class SubjectFacadeService {
    public OrdinarySubjectDTO getOrdinarySubject(Long id) {
        return new OrdinarySubjectDTO(id, "Architecture Design", 1);
    }

    public ElectiveSubjectDTO getElectiveSubject(Long id) {
        return new ElectiveSubjectDTO(id, "3D Modeling");
    }

    public record OrdinarySubjectDTO(Long id, String name, int semester) {

    }

    public record ElectiveSubjectDTO(Long id, String name) {

    }
}
