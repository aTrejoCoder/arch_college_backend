package microservice.schedule_service.Repository;

import microservice.schedule_service.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
        Optional<Group> findByKeyAndSchoolPeriod(String key, String schoolPeriod);
        List<Group> findByOrdinarySubjectId(Long subjectId);
        List<Group> findByClassroom(String classroom);
        List<Group> findByElectiveSubjectId(Long subjectId);
        List<Group> findByClassroomAndSchoolPeriod(String classroom, String schoolPeriod);

        @Query("SELECT g FROM Group g JOIN g.teachers t WHERE t.id = :teacherId AND g.schoolPeriod = :schoolPeriod")
        List<Group> findByTeacherIdAndSchoolPeriod(@Param("teacherId") Long teacherId, @Param("schoolPeriod") String schoolPeriod);
}
