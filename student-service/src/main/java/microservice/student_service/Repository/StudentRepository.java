package microservice.student_service.Repository;

import microservice.student_service.Model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s.id FROM Student s ORDER BY s.id DESC LIMIT 1")
    Optional<Long> findLastId();

    boolean existsByAccountNumber(String accountNumber);
    Optional<Student> findByAccountNumber(String accountNumber);

    Page<Student> findByCareerId(Long careerId, Pageable pageable);
    Page<Student> findByProfessionalLineId(Long professionalLineId, Pageable pageable);
    Page<Student> findByLastName(String lastName, Pageable pageable);
    Page<Student> findByFirstName(String firstName, Pageable pageable);
    Page<Student> findByIncomeGeneration(String incomeGeneration, Pageable pageable);
    Page<Student> findBySemestersCompleted(int semestersCompleted, Pageable pageable);


    Page<Student> findAll(Specification<Student> specification, Pageable pageable);
    Page<Student> findAll(Pageable pageable);

}
