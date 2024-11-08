package microservice.schedule_service.Repository;

import microservice.schedule_service.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
        Optional<Group> findByKey(String key);
        List<Group> findByOrdinarySubjectId(Long subjectId);
        List<Group> findByElectiveSubjectId(Long subjectId);
        List<Group> findByClassroom(String classroom);
        List<Group> findByTeacherId(Long subjectId);

}
