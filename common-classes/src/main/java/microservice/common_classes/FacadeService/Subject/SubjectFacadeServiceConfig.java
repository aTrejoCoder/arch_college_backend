package microservice.common_classes.FacadeService.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

@Configuration
public class SubjectFacadeServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @Qualifier("subjectServiceUrlProvider")
    public Supplier<String> subjectServiceUrlProvider() {
        return () -> {
            List<ServiceInstance> instances = discoveryClient.getInstances("SUBJECT-SERVICE");
            if (instances.isEmpty()) {
                throw new IllegalStateException("subject service is not available");
            }
            return instances.get(0).getUri().toString();
        };
    }

    @Bean
    @Qualifier("subjectFacadeService")
    @Primary
    public SubjectFacadeService SubjectFacadeService(RestTemplate restTemplate,
                                                     @Qualifier("subjectServiceUrlProvider") Supplier<String> subjectServiceUrlProvider) {
        return new SubjectFacadeServiceImpl(restTemplate, subjectServiceUrlProvider);
    }
}
