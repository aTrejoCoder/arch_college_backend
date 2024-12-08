package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByAccountNumber(String accountNumber);
}