package microservice.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.user_service",
		"microservice.common_classes.GlobalExceptions",
		"microservice.common_classes.JWT",
		"microservice.common_classes.Config",
		"microservice.common_classes.FacadeService.Student",
		"microservice.common_classes.FacadeService.Teacher"})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
