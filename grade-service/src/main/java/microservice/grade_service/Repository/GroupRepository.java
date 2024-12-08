package microservice.grade_service.Repository;

import microservice.grade_service.Model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByHeadTeacherAccountNumberAndIsGroupQualifiedFalse(String headTeacherAccountNumber);
    List<Group> findByHeadTeacherAccountNumberAndIsGroupQualifiedTrue(String headTeacherAccountNumber);
    Optional<Group> findByHeadTeacherAccountNumberAndGroupId(String headTeacherAccountNumber, Long groupId);
    Page<Group> findByIsGroupQualifiedFalse(Pageable pageable);

    Optional<Group> findByGroupId(Long groupId);

    @Query("SELECT g FROM Group g WHERE g.headTeacherAccountNumber = :teacherAccountNumber AND g.isGroupQualified = false")
    List<Group> findPendingGroupsByTeacher(String teacherAccountNumber);
}
