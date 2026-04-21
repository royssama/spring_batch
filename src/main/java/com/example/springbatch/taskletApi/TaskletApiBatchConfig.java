package com.example.springbatch.taskletApi;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaskletApiBatchConfig {

    @Bean
    public Step taskletApiCallStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   ExternalApiCallTasklet externalApiCallTasklet) {
        return new StepBuilder("taskletApiCallStep", jobRepository)
                .tasklet(externalApiCallTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job taskletApiCallJob(JobRepository jobRepository, Step taskletApiCallStep) {
        return new JobBuilder("taskletApiCallJob", jobRepository)
                .start(taskletApiCallStep)
                .build();
    }
}
