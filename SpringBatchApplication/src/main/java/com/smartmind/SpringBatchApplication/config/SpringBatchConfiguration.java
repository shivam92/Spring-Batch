package com.smartmind.SpringBatchApplication.config;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.smartmind.SpringBatchApplication.entity.EmployeeEntity;
import com.smartmind.SpringBatchApplication.processor.CustomerProcessor;
import com.smartmind.SpringBatchApplication.repo.EmployeeRepo;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SpringBatchConfiguration {
@Autowired
EmployeeRepo employeeRepo;
	
	@Bean
	public Job runJob(JobRepository jobRepository,PlatformTransactionManager transactionManger) {
		
		return new JobBuilder("Employee-Data-Job",jobRepository).flow(step1(jobRepository,transactionManger))
				.end().build();
		
	}
	@Bean
	public Step step1(JobRepository jobRepository,PlatformTransactionManager transactionManger) {
		
		return new StepBuilder("Employee-Data-step1",jobRepository).
				<EmployeeEntity,EmployeeEntity>chunk(10,transactionManger)
				.reader(fileReader())
				.processor(processor())
				.writer(employeeDataWrite())
				.taskExecutor(taskExecutor())
				.build();
		
	}
	@Bean
	public CustomerProcessor processor() {
	  return new CustomerProcessor();
	}
	@Bean
	public FlatFileItemReader<EmployeeEntity> fileReader(){
		return new FlatFileItemReaderBuilder<EmployeeEntity>()
				.name("EmployeeReader")
				.resource(new FileSystemResource("src/main/resources/employee.csv"))
				.delimited()
				.names("id","firstname","lastname")
				.linesToSkip(1)
				.targetType(EmployeeEntity.class)
							
				.build();
	}
	@Bean
	public RepositoryItemWriter employeeDataWrite(){
		return new RepositoryItemWriterBuilder().
				repository(employeeRepo)
				.methodName("save").
				build();
	}
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor task=new SimpleAsyncTaskExecutor();
		task.setConcurrencyLimit(10);
	        return task;
	}
}
