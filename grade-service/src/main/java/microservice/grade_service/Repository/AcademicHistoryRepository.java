package microservice.grade_service.Repository;

import microservice.grade_service.Model.AcademicHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AcademicHistoryRepository extends MongoRepository<AcademicHistory, String> {
    Optional<AcademicHistory> findByStudentAccountNumber(String accountNumber);
}