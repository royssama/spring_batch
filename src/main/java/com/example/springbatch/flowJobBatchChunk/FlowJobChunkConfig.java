package com.example.springbatch.flowJobBatchChunk;

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
public class FlowJobChunkConfig {

    @Bean
    public Step flowChunkRouteStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   FlowChunkRouteTasklet flowChunkRouteTasklet) {
        return new StepBuilder("flowChunkRouteStep", jobRepository)
                .tasklet(flowChunkRouteTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step flowChunkProcessStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     FlowChunkReader flowChunkReader,
                                     FlowChunkProcessor flowChunkProcessor,
                                     FlowChunkWriter flowChunkWriter) {
        return new StepBuilder("flowChunkProcessStep", jobRepository)
                // chunk 사이즈 3: 3개씩 읽고/처리하고/쓰기
                .<FlowChunkItem, FlowChunkItem>chunk(3, transactionManager)
                .reader(flowChunkReader)
                .processor(flowChunkProcessor)
                .writer(flowChunkWriter)
                .build();
    }

    @Bean
    public Step flowChunkSummaryStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     FlowChunkSummaryTasklet flowChunkSummaryTasklet) {
        return new StepBuilder("flowChunkSummaryStep", jobRepository)
                .tasklet(flowChunkSummaryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step flowChunkCleanupStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     FlowChunkCleanupTasklet flowChunkCleanupTasklet) {
        return new StepBuilder("flowChunkCleanupStep", jobRepository)
                .tasklet(flowChunkCleanupTasklet, transactionManager)
                .build();
    }

    @Bean
    public Flow flowChunkBranchFlow(Step flowChunkRouteStep,
                                    Step flowChunkProcessStep,
                                    Step flowChunkSummaryStep,
                                    Step flowChunkCleanupStep) {
        // 초보자용 핵심:
        // state   : flowChunkRouteStep
        // pattern : PROCESS_PATH / SKIP_PATH / * (기타)
        // next    : process, cleanup, summary step
        return new FlowBuilder<Flow>("flowChunkBranchFlow")
                .start(flowChunkRouteStep)
                    .on("PROCESS_PATH").to(flowChunkProcessStep)
                        .next(flowChunkSummaryStep) // 처리 분기로 갔으면 요약 step까지 이어서 실행
                .from(flowChunkRouteStep)
                    .on("SKIP_PATH").to(flowChunkCleanupStep)
                .from(flowChunkRouteStep)
                    .on("*").to(flowChunkSummaryStep)
                .end();
    }

    @Bean
    public Job flowChunkJob(JobRepository jobRepository, Flow flowChunkBranchFlow) {
        return new JobBuilder("flowChunkJob", jobRepository)
                .start(flowChunkBranchFlow)
                .end()
                .build();
    }
}
