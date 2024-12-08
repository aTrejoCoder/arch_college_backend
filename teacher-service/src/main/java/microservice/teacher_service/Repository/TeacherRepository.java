package microservice.teacher_service.Repository;

import microservice.common_classes.Utils.Teacher.Title;
import microservice.teacher_service.Model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT s.id FROM Teacher s ORDER BY s.id DESC")
    Optional<Long> findLastId();

    List<Teacher> findByIdIn(Set<Long> idSet);

    boolean existsByAccountNumber(String accountNumber);

    Optional<Teacher> findByAccountNumber(String accountNumber);

    Page<Teacher> findByTitle(Title title, Pageable pageable);

}
