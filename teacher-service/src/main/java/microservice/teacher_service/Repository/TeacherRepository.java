package microservice.teacher_service.Repository;

import microservice.teacher_service.Model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT s.teacherId FROM Teacher s ORDER BY s.teacherId DESC")
    Optional<Long> findLastId();

    Optional<Teacher> findByAccountNumber(String accountNumber);

    Page<Teacher> findByTitle(String title, Pageable pageable);

}
