package com.example.springbatch.flowJobBatchTasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FlowJobTaskletConfig {

    @Bean
    public Step flowTaskletRouteStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     FlowTaskletRouteTasklet flowTaskletRouteTasklet) {
        return new StepBuilder("flowTaskletRouteStep", jobRepository)
                .tasklet(flowTaskletRouteTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step flowTaskletSuccessStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       FlowTaskletSuccessTasklet flowTaskletSuccessTasklet) {
        return new StepBuilder("flowTaskletSuccessStep", jobRepository)
                .tasklet(flowTaskletSuccessTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step flowTaskletRetryStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     FlowTaskletRetryTasklet flowTaskletRetryTasklet) {
        return new StepBuilder("flowTaskletRetryStep", jobRepository)
                .tasklet(flowTaskletRetryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step flowTaskletStopStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    FlowTaskletStopTasklet flowTaskletStopTasklet) {
        return new StepBuilder("flowTaskletStopStep", jobRepository)
                .tasklet(flowTaskletStopTasklet, transactionManager)
                .build();
    }

    @Bean
    public Flow flowTaskletBranchFlow(Step flowTaskletRouteStep,
                                      Step flowTaskletSuccessStep,
                                      Step flowTaskletRetryStep,
                                      Step flowTaskletStopStep) {
        // 초보자용 핵심 정리:
        // state   : flowTaskletRouteStep
        // pattern : RETRY_PATH / STOP_PATH / SUCCESS_PATH / * (예외 fallback)
        // next    : 각각 retry, stop, success step
        return new FlowBuilder<Flow>("flowTaskletBranchFlow")
                .start(flowTaskletRouteStep)
                    .on("RETRY_PATH").to(flowTaskletRetryStep)
                .from(flowTaskletRouteStep)
                    .on("STOP_PATH").to(flowTaskletStopStep)
                .from(flowTaskletRouteStep)
                    .on("SUCCESS_PATH").to(flowTaskletSuccessStep)
                .from(flowTaskletRouteStep)
                    .on("*").to(flowTaskletSuccessStep)
                .end();
    }

    @Bean
    public Job flowTaskletJob(JobRepository jobRepository, Flow flowTaskletBranchFlow) {
        return new JobBuilder("flowTaskletJob", jobRepository)
                .start(flowTaskletBranchFlow)
                .end()
                .build();
    }
}
