package microservice.common_classes.FacadeService.Grade;

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
public class GradeServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @Qualifier("gradeServiceUrlProvider")
    public Supplier<String> gradeServiceUrlProvider() {
        return () -> {
            List<ServiceInstance> instances = discoveryClient.getInstances("GRADE-SERVICE");
            if (instances.isEmpty()) {
                throw new IllegalStateException("grade service is not available");
            }
            return instances.get(0).getUri().toString();
        };
    }

    @Bean
    @Qualifier("gradeService")
    public GradeFacadeService gradeService(RestTemplate restTemplate,
                                                 @Qualifier("gradeServiceUrlProvider") Supplier<String> gradeServiceUrlProvider) {
        return new GradeFacadeServiceImpl(restTemplate, gradeServiceUrlProvider);
    }
}