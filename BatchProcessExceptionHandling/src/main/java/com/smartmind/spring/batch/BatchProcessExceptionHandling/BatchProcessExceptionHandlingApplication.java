package com.smartmind.spring.batch.BatchProcessExceptionHandling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.smartmind.spring.batch")
@EntityScan(basePackages = "com.smartmind.spring.batch.entity")
@EnableJpaRepositories(basePackages = "com.smartmind.spring.batch")
@ComponentScan(basePackages = "com.smartmind.spring.batch")
public class BatchProcessExceptionHandlingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchProcessExceptionHandlingApplication.class, args);
	}

}
