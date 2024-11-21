package microservice.common_classes.FacadeService.Group;

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
public class GroupServiceConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    @Qualifier("groupServiceUrlProvider")
    public Supplier<String> groupServiceUrlProvider() {
        return () -> {
            List<ServiceInstance> instances = discoveryClient.getInstances("GROUP-SERVICE");
            if (instances.isEmpty()) {
                throw new IllegalStateException("group service is not available");
            }
            return instances.get(0).getUri().toString();
        };
    }

    @Bean
    @Qualifier("groupFacadeService")
    public GroupFacadeService groupFacadeService(RestTemplate restTemplate,
                                                 @Qualifier("groupServiceUrlProvider") Supplier<String> groupServiceUrlProvider) {
        return new GroupFacadeServiceImpl(restTemplate, groupServiceUrlProvider);
    }
}