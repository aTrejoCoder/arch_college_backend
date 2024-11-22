package microservice.enrollment_service.Service;

import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.OrdinarySubjectDTO;
import microservice.common_classes.FacadeService.Grade.GradeFacadeService;
import microservice.common_classes.FacadeService.Grade.GradeFacadeServiceImpl;
import microservice.common_classes.FacadeService.Group.GroupFacadeService;
import microservice.common_classes.FacadeService.Student.StudentFacadeService;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.Utils.Result;
import microservice.enrollment_service.DTOs.EnrollmentInsertDTO;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EnrollmentRelationshipServiceImpl implements EnrollmentRelationshipService {
    public final StudentFacadeService studentFacadeService;
    public final GroupFacadeService groupFacadeService;
    public final GradeFacadeService gradeFacadeService;
    public final SubjectFacadeService subjectFacadeService;

    public EnrollmentRelationshipServiceImpl(@Qualifier("StudentFacadeServiceImpl") StudentFacadeService studentFacadeService,
                                             @Qualifier("GroupFacadeServiceImpl") GroupFacadeService groupFacadeService,
                                             @Qualifier("GradeFacadeServiceImpl") GradeFacadeService gradeFacadeService,
                                             @Qualifier("SubjectFacadeServiceImpl") SubjectFacadeService subjectFacadeService) {
        this.studentFacadeService = studentFacadeService;
        this.groupFacadeService = groupFacadeService;
        this.gradeFacadeService = gradeFacadeService;
        this.subjectFacadeService = subjectFacadeService;
    }

    public Result<EnrollmentRelationshipDTO> validateAndGetRelationships(EnrollmentInsertDTO enrollmentInsertDTO) {
        CompletableFuture<StudentDTO> studentFuture = studentFacadeService.getStudentByAccountNumber(enrollmentInsertDTO.getStudentAccountNumber());
        CompletableFuture<GroupDTO> groupFuture = groupFacadeService.getGroupById(enrollmentInsertDTO.getGroupId());
        CompletableFuture<List<GradeDTO>> studentGradeFuture = gradeFacadeService.getGradesByStudentId(enrollmentInsertDTO.getGroupId());

        return CompletableFuture.allOf(studentFuture, groupFuture, studentGradeFuture).thenApply(v -> {
            StudentDTO studentDTO = studentFuture.join();
            GroupDTO groupDTO = groupFuture.join();
            List<GradeDTO> studentGrades = studentGradeFuture.join();

            if(studentDTO == null) {
                return Result.<EnrollmentRelationshipDTO>error("Can't bring Student");
            }

            if(groupDTO == null) {
                return Result.<EnrollmentRelationshipDTO>error("Invalid Group");
            }

            if (studentGrades == null) {
                return Result.<EnrollmentRelationshipDTO>error("Invalid Group");
            }

            if (groupDTO.getElectiveSubjectId() !=  null && groupDTO.getOrdinarySubjectId() == null) {
                ElectiveSubjectDTO electiveSubjectDTO = subjectFacadeService.getElectiveSubjectById(groupDTO.getElectiveSubjectId()).join();
            } else if (groupDTO.getOrdinarySubjectId() != null && groupDTO.getElectiveSubjectId() == null ) {
                OrdinarySubjectDTO ordinarySubjectDTO = subjectFacadeService.getOrdinarySubjectById(groupDTO.getOrdinarySubjectId()).join();
            } else {
                return Result.<EnrollmentRelationshipDTO>error("No Subject Id Provided");
            }


            return Result.success(new EnrollmentRelationshipDTO());
        }).join();
    }

    @Override
    public Result<Void> takeSpot(Long groupId) {
       Result<Void> spotResult = groupFacadeService.reduceSpot(groupId).join();
       if (!spotResult.isSuccess()) {
           return Result.error(spotResult.getErrorMessage());
       }

       return Result.success();
    }
}
