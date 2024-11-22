package microservice.subject_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI subjectServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subject Service API")
                        .description("Microservice for managing subjects, including ordinary subjects, elective subjects, and professional lines.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Alexis Trejo")
                                .url("https://web.com")
                                .email("contact@yourwebsite.com"))
                        .termsOfService("https://terms.com/terms")
                );
    }
}
