package microservice.student_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@EnableRabbit
@ComponentScan(basePackages = {"microservice.student_service",
		"microservice.common_classes.GlobalExceptions",
		"microservice.common_classes.JWT",
		"microservice.common_classes.FacadeService.Subject",
		"microservice.common_classes.FacadeService.Grade"

})
public class StudentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceApplication.class, args);
	}

}
