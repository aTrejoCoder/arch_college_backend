package microservice.student_service.Repository;

import microservice.student_service.Model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s.id FROM Student s ORDER BY s.id DESC LIMIT 1")
    Optional<Long> findLastId();

    Optional<Student> findByAccountNumber(String accountNumber);
}
