package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GradRepository extends MongoRepository<Grade, String> {
}