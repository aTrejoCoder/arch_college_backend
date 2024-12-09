package microservice.grade_service.Documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gradeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Grade Service API")
                        .description("Grade Microservice that manages student grades, group qualifications, and academic history updates.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Support Team")
                                .url("https://company.com")
                                .email("support@company.com"))
                        .termsOfService("https://company.com/terms")
                );
    }
}
