package microservice.enrollment_service.Documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enrollmentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enrollment Service API")
                        .description("Microservice for managing student enrollments, group relationships, and subject assignments. Provides endpoints for enrollment management, validations, and entity relationships.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Technical Support")
                                .url("https://my-university.com/support")
                                .email("support@my-university.com"))
                        .termsOfService("https://my-university.com/terms")
                );
    }
}
