package microservice.enrollment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.enrollment_service",
		"microservice.common_classes.GlobalExceptions",
		"microservice.common_classes.FacadeService.Student",
		"microservice.common_classes.FacadeService.Grade",
		"microservice.common_classes.FacadeService.Subject",
		"microservice.common_classes.FacadeService.Group"})
public class EnrollmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnrollmentServiceApplication.class, args);
	}

}
