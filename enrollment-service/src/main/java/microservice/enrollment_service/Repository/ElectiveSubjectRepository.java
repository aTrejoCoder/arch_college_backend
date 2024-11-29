package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ElectiveSubjectRepository extends MongoRepository<ElectiveSubject, String> {
}