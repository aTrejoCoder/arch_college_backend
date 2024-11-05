package microservice.subject_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.subject_service",
		"microservice.common_classes.GlobalExceptions"})
public class SubjectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubjectServiceApplication.class, args);
	}

}
