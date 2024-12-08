package microservice.student_service.Utils;

import microservice.common_classes.Utils.Response.Result;
import microservice.common_classes.Utils.Student.StudentFilter;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class StudentFinderValidator {

    public Result<StudentFilter> validateStudentFilter(String filter) {
        boolean isValidFilter = StudentFilter.isValid(filter);
        if (!isValidFilter) {
            return Result.error("Invalid filter: " + filter + ". Allowed Filters:" + Arrays.toString(StudentFilter.getFinderFilters()));
        }

        StudentFilter studentFilter = StudentFilter.valueOf(filter.toUpperCase());

        return Result.success(studentFilter);
    }

}
