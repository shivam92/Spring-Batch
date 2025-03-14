package com.smartmind.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.smartmind.spring.batch.entity.EmployeeEntity;
import com.smartmind.spring.batch.processor.FileItemProcessor;
import com.smartmind.spring.batch.repo.EmployeeRepo;

import jakarta.transaction.TransactionManager;

@Configuration
public class BatchConfig {
	
	@Autowired
	EmployeeRepo employeeRepo; 
	@Autowired 
	FileItemProcessor fileItemProcessor;
	
	@Bean
	public FlatFileItemReader<EmployeeEntity>fileReader(){
		return new FlatFileItemReaderBuilder().name("fileReader")
				.resource(new FileSystemResource("src/main/resources/employee.csv"))
				.linesToSkip(1)
				.delimited()
				.names("id","name","age")
				.targetType(EmployeeEntity.class)
				.build();
	}
	@Bean
	public RepositoryItemWriter<EmployeeEntity> itemWrite() {
		return new RepositoryItemWriterBuilder().repository(employeeRepo)
				.methodName("save").build();
	}
	
	@Bean
	public Step step1(JobRepository jobRepository,PlatformTransactionManager transaction) {
		return new StepBuilder("Step1", jobRepository)
				.<EmployeeEntity,EmployeeEntity>chunk(10,transaction)
				.reader(fileReader())
				.processor(fileItemProcessor)
				.writer(itemWrite())
				.faultTolerant()
				.skipLimit(100)
				.skip(Exception.class)
				.taskExecutor(task())
				.build();
	}
	@Bean
	public TaskExecutor task() {
		SimpleAsyncTaskExecutor task=new SimpleAsyncTaskExecutor();
		task.setConcurrencyLimit(10);
		return task;
	}
	@Bean
	public Job runJob(JobRepository jobrepo,PlatformTransactionManager transaction) {
		return new JobBuilder("job1",jobrepo)
				.flow(step1(jobrepo,transaction))
				.end()
				.build();
		
	}
	
	

}
