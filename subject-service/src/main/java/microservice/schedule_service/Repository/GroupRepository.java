package microservice.schedule_service.Repository;

import microservice.schedule_service.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
        Optional<Group> findByKeyAndSemester(String key,  String semester);
        List<Group> findByOrdinarySubjectId(Long subjectId);
        List<Group> findByElectiveSubjectId(Long subjectId);
        List<Group> findByClassroomAndSemester(String classroom,  String semester);
        List<Group> findByTeacherIdAndSemester(Long subjectId, String semester);

}
