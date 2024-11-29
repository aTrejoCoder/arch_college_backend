package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}