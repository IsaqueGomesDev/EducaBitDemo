package com.educabit.educabit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.boot.context.properties.EnableConfigurationProperties(com.educabit.educabit.config.FileStorageProperties.class)
public class EducabitApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducabitApplication.class, args);
	}

}
