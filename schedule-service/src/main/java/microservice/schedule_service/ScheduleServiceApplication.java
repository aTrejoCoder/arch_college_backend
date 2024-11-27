package microservice.schedule_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.common_classes.GlobalExceptions",
		"microservice.common_classes.JWT",
		"microservice.schedule_service",
		"microservice.common_classes.FacadeService.Teacher",
		"microservice.common_classes.FacadeService.Subject"})
public class ScheduleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleServiceApplication.class, args);
	}

}
