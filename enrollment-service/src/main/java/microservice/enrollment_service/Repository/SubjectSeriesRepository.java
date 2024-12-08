package microservice.enrollment_service.Repository;

import microservice.enrollment_service.Model.Preload.Student;
import microservice.enrollment_service.Model.Preload.SubjectSeries;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectSeriesRepository extends MongoRepository<SubjectSeries, String> {
}