package microservice.schedule_service.Repository;

import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.Utils.SubjectType;
import microservice.schedule_service.Models.Group;
import microservice.schedule_service.Utils.GroupFilters;
import microservice.schedule_service.Utils.GroupSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
        Optional<Group> findByGroupKeyAndSchoolPeriod(String groupKey, String schoolPeriod);

        List<Group> findBySubjectIdAndSubjectTypeAndSchoolPeriod(Long subjectId, SubjectType subjectType, String schoolPeriod);

        List<Group> findByIdIn(List<Long> idList);

        Page<Group> findBySchoolPeriod(String schoolPeriod, Pageable pageable);

        List<Group> findByClassroomAndSchoolPeriod(String classroom, String schoolPeriod);

        @Query("SELECT g FROM Group g JOIN g.teachers t WHERE t.id = :teacherId AND g.schoolPeriod = :schoolPeriod")
        List<Group> findByTeacherIdAndSchoolPeriod(@Param("teacherId") Long teacherId, @Param("schoolPeriod") String schoolPeriod);

        @Query("SELECT g FROM Group g WHERE g.classroom LIKE :prefix% AND g.schoolPeriod = :schoolPeriod")
        List<Group> findByClassroomPrefix(@Param("prefix") String prefix, String schoolPeriod);

        Page<Group> findAll(Pageable pageable);
}
