package microservice.schedule_service.Repository;

import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
