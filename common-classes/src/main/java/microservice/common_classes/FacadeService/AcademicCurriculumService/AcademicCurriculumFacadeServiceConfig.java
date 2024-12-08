package microservice.common_classes.FacadeService.AcademicCurriculumService;

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
public class AcademicCurriculumFacadeServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @Qualifier("academicCurriculumServiceUrlProvider")
    public Supplier<String> AcademicCurriculumServiceUrlProvider() {
        return () -> {
            List<ServiceInstance> instances = discoveryClient.getInstances("ACADEMIC_CURRICULUM-SERVICE");
            if (instances.isEmpty()) {
                throw new IllegalStateException("academicCurriculum service is not available");
            }
            return instances.get(0).getUri().toString();
        };
    }

    @Bean
    @Qualifier("academicCurriculumFacadeService")
    @Primary
    public AcademicCurriculumFacadeService AcademicCurriculumFacadeService(RestTemplate restTemplate,
                                                     @Qualifier("academicCurriculumServiceUrlProvider") Supplier<String> academicCurriculumServiceUrlProvider) {
        return new AcademicCurriculumFacadeServiceImpl(restTemplate, academicCurriculumServiceUrlProvider);
    }
}
