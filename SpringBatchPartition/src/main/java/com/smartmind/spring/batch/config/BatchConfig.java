package com.smartmind.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.smartmind.spring.batch.entity.EmployeeEntity;
import com.smartmind.spring.batch.partition.config.CustomerPartitioner;
import com.smartmind.spring.batch.processor.FileItemProcessor;
import com.smartmind.spring.batch.repo.EmployeeRepo;

@Configuration
public class BatchConfig {
	
	@Autowired
	EmployeeRepo employeeRepo;
	@Autowired
	FileItemProcessor processor;
	
	@Autowired
	CustomerPartitioner partitioner;
	@Bean
	public FlatFileItemReader<EmployeeEntity>fileReader(){
		return new FlatFileItemReaderBuilder<EmployeeEntity>().name("reader")
				.resource(new FileSystemResource("src/main/resources/employee.csv"))
				.delimited()
				.names("id","name","age")
				.targetType(EmployeeEntity.class)
				.linesToSkip(1)
				.build();
	}

	@Bean
	public RepositoryItemWriter<EmployeeEntity>itemWriter(){
		return new RepositoryItemWriterBuilder<EmployeeEntity>()
				.repository(employeeRepo)
				.methodName("save")
				.build();
	}
	
	@Bean
	public PartitionHandler partitionHandler(JobRepository repo,PlatformTransactionManager transaction) {
		TaskExecutorPartitionHandler excuter=new TaskExecutorPartitionHandler();
		excuter.setGridSize(2);
		excuter.setTaskExecutor(taskExecutor());
		excuter.setStep(slaveStep(repo,transaction));
		
		return excuter;
	}
	@Bean
	public Step slaveStep(JobRepository repo,PlatformTransactionManager transaction) {
		return  new StepBuilder("slaveStep",repo)
		.<EmployeeEntity,EmployeeEntity>chunk(10,transaction)
		.reader(fileReader())
		.processor(processor)
		.writer(itemWriter())
		//.taskExecutor(taskExecutor())
		.build();
	}
	@Bean
	public Step masterStep(JobRepository repo,PlatformTransactionManager transaction) {
		return  new StepBuilder("masterStep",repo)
		.partitioner(slaveStep(repo,transaction).getName(), partitioner)
		.partitionHandler(partitionHandler(repo,transaction))
		.build();
	}
	@Bean
	public Job job1(JobRepository repo,PlatformTransactionManager transaction) {
		return new JobBuilder("job1",repo)
				.flow(masterStep(repo, transaction))
				.end().build();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool=new ThreadPoolTaskExecutor();
		pool.setMaxPoolSize(2);
		pool.setQueueCapacity(2);
		pool.setCorePoolSize(2);
		return pool;
	}
	

}
