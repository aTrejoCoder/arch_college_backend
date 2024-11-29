package microservice.student_service.Utils;

import microservice.common_classes.Utils.Student.StudentFilter;
import microservice.student_service.Model.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {

    public static Specification<Student> withFilter(StudentFilter filter, String param) {
        return (root, query, criteriaBuilder) -> {
            switch (filter) {
                case CAREER:
                    return criteriaBuilder.equal(root.get("careerId"), Long.valueOf(param));
                case PROFESSIONAL_LINE:
                    return criteriaBuilder.equal(root.get("professionalLineId"), Long.valueOf(param));
                case LAST_NAME:
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + param.toLowerCase() + "%");
                case FIRST_NAME:
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + param.toLowerCase() + "%");
                case INCOME_GENERATION:
                    return criteriaBuilder.equal(root.get("incomeGeneration"), param);
                case SEMESTERS_COMPLETED:
                    return criteriaBuilder.equal(root.get("semestersCompleted"), Integer.valueOf(param));
                default:
                    return null;
            }
        };
    }

    public static Specification<Student> withFilter(StudentFilter filter) {
        return (root, query, criteriaBuilder) -> {
            switch (filter) {
                case LAST_NAME:
                    return criteriaBuilder.isNotNull(root.get("lastName"));
                case FIRST_NAME:
                    return criteriaBuilder.isNotNull(root.get("firstName"));
                case CAREER:
                    return criteriaBuilder.isNotNull(root.get("careerId"));
                case PROFESSIONAL_LINE:
                    return criteriaBuilder.isNotNull(root.get("professionalLineId"));
                case INCOME_GENERATION:
                    return criteriaBuilder.isNotNull(root.get("incomeGeneration"));
                case SEMESTERS_COMPLETED:
                    return criteriaBuilder.isNotNull(root.get("semestersCompleted"));
                default:
                    return null;
            }
        };
    }
}
