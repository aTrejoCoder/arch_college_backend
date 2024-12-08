package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, Long> {

    Optional<Group> findByGroupKeyAndSubjectKey(String groupKey, String subjectKey);
}