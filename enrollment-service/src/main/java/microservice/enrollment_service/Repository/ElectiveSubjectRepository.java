package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.ElectiveSubject;
import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ElectiveSubjectRepository extends MongoRepository<ElectiveSubject, String> {
    Optional<ElectiveSubject> findByKey(String key);
}