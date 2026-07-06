package com.hackathon.healthcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthCardApplication.class, args);
	}

}
