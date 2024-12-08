package microservice.academic_curriculum_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.academic_curriculum_service",
		"microservice.common_classes.GlobalExceptions",
		"microservice.common_classes.JWT"
})
public class AcademicCurriculumServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademicCurriculumServiceApplication.class, args);
	}

}
