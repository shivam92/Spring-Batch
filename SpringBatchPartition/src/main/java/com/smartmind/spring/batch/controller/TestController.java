package com.smartmind.spring.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TestController {

	@Autowired
	JobLauncher jobLauncher; 
	@Autowired
	Job job;
	
	@GetMapping("batchTest")
	public String batchTest() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters paramteres=new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
		jobLauncher.run(job, paramteres);
		return "Success";
	}
	
}
