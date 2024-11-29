package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
}