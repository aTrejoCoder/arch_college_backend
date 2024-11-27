package microservice.enrollment_service.Service;

import lombok.extern.slf4j.Slf4j;
import microservice.common_classes.DTOs.Enrollment.EnrollmentInsertDTO;
import microservice.common_classes.DTOs.Grade.GradeDTO;
import microservice.common_classes.DTOs.Group.GroupDTO;
import microservice.common_classes.DTOs.Student.StudentDTO;
import microservice.common_classes.DTOs.Subject.ElectiveSubjectDTO;
import microservice.common_classes.DTOs.Subject.ObligatorySubjectDTO;
import microservice.common_classes.FacadeService.Grade.GradeFacadeService;
import microservice.common_classes.FacadeService.Group.GroupFacadeService;
import microservice.common_classes.FacadeService.Student.StudentFacadeService;
import microservice.common_classes.FacadeService.Subject.SubjectFacadeService;
import microservice.common_classes.Utils.Response.Result;
import microservice.enrollment_service.DTOs.EnrollmentRelationshipDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
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

    public Result<EnrollmentRelationshipDTO> validateAndGetRelationships(EnrollmentInsertDTO enrollmentInsertDTO, String studentAccountNumber) {
        CompletableFuture<StudentDTO> studentFuture = studentFacadeService.getStudentByAccountNumber(studentAccountNumber);
        CompletableFuture<GroupDTO> groupFuture = groupFacadeService.getCurrentGroupByKey(enrollmentInsertDTO.getGroupKey());
        CompletableFuture<List<GradeDTO>> studentGradeFuture = gradeFacadeService.getGradesByStudentAccountNumber(studentAccountNumber);

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
                studentGrades = new ArrayList<>();
            }

            if (groupDTO.getElectiveSubjectId() !=  null && groupDTO.getObligatorySubjectId() == null) {
                ElectiveSubjectDTO electiveSubjectDTO = subjectFacadeService.getElectiveSubjectById(groupDTO.getElectiveSubjectId()).join();
                return Result.success(new EnrollmentRelationshipDTO(studentDTO,groupDTO, electiveSubjectDTO, studentGrades));
            } else if (groupDTO.getObligatorySubjectId() != null && groupDTO.getElectiveSubjectId() == null ) {
                ObligatorySubjectDTO ordinarySubjectDTO = subjectFacadeService.getOrdinarySubjectById(groupDTO.getObligatorySubjectId()).join();
                return Result.success(new EnrollmentRelationshipDTO(studentDTO,groupDTO, ordinarySubjectDTO, studentGrades));

            } else {
                return Result.<EnrollmentRelationshipDTO>error("No Subject Id Provided");
            }



        }).join();
    }

    @Override
    public Result<Void> takeSpot(String groupKey) {
       Result<Void> spotResult = groupFacadeService.takeSpot(groupKey).join();
       if (!spotResult.isSuccess()) {
           return Result.error(spotResult.getErrorMessage());
       }

       return Result.success();
    }
}
