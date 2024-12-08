package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.ObligatorySubject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObligatorySubjectRepository extends MongoRepository<ObligatorySubject, Long> {

    @Query("{ 'key': ?0 }")
    Optional<ObligatorySubject> findByKey(String key);
}