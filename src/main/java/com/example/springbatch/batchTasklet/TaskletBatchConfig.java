package com.example.springbatch.batchTasklet;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaskletBatchConfig {

    private final TaskletBatchMapper taskletBatchMapper;

    public TaskletBatchConfig(TaskletBatchMapper taskletBatchMapper) {
        this.taskletBatchMapper = taskletBatchMapper;
    }

    @PostConstruct
    public void initSchema() {
        taskletBatchMapper.createTaskletTableIfNotExists();
    }

    @Bean
    public Step taskletDbStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              DbLoggingTasklet dbLoggingTasklet) {
        return new StepBuilder("taskletDbStep", jobRepository)
                .tasklet(dbLoggingTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step taskletSelectStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  TaskletSelectTasklet taskletSelectTasklet) {
        return new StepBuilder("taskletSelectStep", jobRepository)
                .tasklet(taskletSelectTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job taskletDbJob(JobRepository jobRepository, Step taskletDbStep, Step taskletSelectStep) {
        return new JobBuilder("taskletDbJob", jobRepository)
                .start(taskletDbStep)
                .next(taskletSelectStep)
                .build();
    }
}
