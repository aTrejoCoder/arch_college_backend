package microservice.grade_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.grade_service",
		"microservice.common_classes.GlobalExceptions",
		"microservice.common_classes.JWT",
		"microservice.common_classes.FacadeService.Group",
		"microservice.common_classes.FacadeService.AcademicCurriculumService",
		"microservice.common_classes.FacadeService.Student",
})
public class GradeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradeServiceApplication.class, args);
	}

}
