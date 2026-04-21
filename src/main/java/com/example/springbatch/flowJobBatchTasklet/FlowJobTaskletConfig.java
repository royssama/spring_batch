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
        // Flow 이름을 "flowTaskletBranchFlow"로 생성한다.
        // 이 Flow는 route step의 ExitStatus 값에 따라 다음 step을 분기한다.
        return new FlowBuilder<Flow>("flowTaskletBranchFlow")
                // 분기 기준이 되는 시작 step: flowTaskletRouteStep 실행
                .start(flowTaskletRouteStep)
                    // route step 결과가 "RETRY_PATH"이면 retry step으로 이동
                    .on("RETRY_PATH").to(flowTaskletRetryStep)
                // 같은 route step을 기준으로 추가 분기 조건을 이어서 정의
                .from(flowTaskletRouteStep)
                    // route step 결과가 "STOP_PATH"이면 stop step으로 이동
                    .on("STOP_PATH").to(flowTaskletStopStep)
                // 위 두 조건에 걸리지 않는 나머지 모든 결과값에 대한 기본 분기
                .from(flowTaskletRouteStep)
                    // * 는 wildcard: 나머지 케이스는 success step으로 이동
                    .on("*").to(flowTaskletSuccessStep)
                // Flow 정의 종료
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
