package microservice.common_classes.FacadeService.Teacher;

import microservice.common_classes.FacadeService.Student.StudentFacadeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

@Configuration
public class TeacherFacadeServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @Qualifier("teacherServiceUrlProvider")
    public Supplier<String> teacherServiceUrlProvider() {
        return () -> {
            List<ServiceInstance> instances = discoveryClient.getInstances("TEACHER-SERVICE");
            if (instances.isEmpty()) {
                throw new IllegalStateException("teacher service is not available");
            }
            return instances.get(0).getUri().toString();
        };
    }

    @Bean
    @Qualifier("teacherFacadeService")
    public TeacherFacadeService TeacherFacadeService(RestTemplate restTemplate,
                                                     @Qualifier("teacherServiceUrlProvider") Supplier<String> teacherServiceUrlProvider) {
        return new StudentFacadeServiceImpl(restTemplate, teacherServiceUrlProvider);
    }
}
