package com.example.springbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class GeneralBatchConfig {

    @Bean
    public Step generalSampleStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  GeneralSampleTasklet generalSampleTasklet) {
        return new StepBuilder("generalSampleStep", jobRepository)
                .tasklet(generalSampleTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job generalSampleJob(JobRepository jobRepository, Step generalSampleStep) {
        return new JobBuilder("generalSampleJob", jobRepository)
                .start(generalSampleStep)
                .build();
    }
}
