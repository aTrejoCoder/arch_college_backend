package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GradeRepository extends MongoRepository<Grade, String> {
    List<Grade> findByStudentAccountNumber(String studentAccountNumber);
}