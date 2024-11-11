package microservice.common_classes.FacadeService.Student;

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
public class StudentFacadeServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @Qualifier("studentServiceUrlProvider")
    public Supplier<String> studentServiceUrlProvider() {
        return () -> {
            List<ServiceInstance> instances = discoveryClient.getInstances("STUDENT-SERVICE");
            if (instances.isEmpty()) {
                throw new IllegalStateException("student service is not available");
            }
            return instances.get(0).getUri().toString();
        };
    }

    @Bean
    @Qualifier("studentFacadeService")
    public StudentFacadeService StudentFacadeService(RestTemplate restTemplate,
                                                     @Qualifier("studentServiceUrlProvider") Supplier<String> studentServiceUrlProvider) {
        return new StudentFacadeServiceImpl(restTemplate, studentServiceUrlProvider);
    }
}