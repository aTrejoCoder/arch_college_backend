package microservice.schedule_service.Utils;

import jakarta.persistence.criteria.Predicate;
import microservice.schedule_service.Models.Group;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GroupSpecification {

    public static Specification<Group> withFilters(GroupFinderFilter groupFinderFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            var filters = groupFinderFilter.getFilters();

            filters.forEach((filter, value) -> {
                if (value != null) {
                    switch (filter) {
                        case SCHOOL_PERIOD -> predicates.add(criteriaBuilder.equal(root.get("schoolPeriod"), value));
                        case SUBJECT_TYPE -> predicates.add(criteriaBuilder.equal(root.get("subjectType"), value));
                        case SUBJECT_KEY -> predicates.add(criteriaBuilder.like(root.get("subjectKey"), "%" + value + "%"));
                        case CLASSROOM -> predicates.add(criteriaBuilder.equal(root.get("classroom"), value));
                    }
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

