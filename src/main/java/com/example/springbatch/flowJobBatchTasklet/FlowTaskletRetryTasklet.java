package com.example.springbatch.flowJobBatchTasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowTaskletRetryTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowTaskletRetryTasklet.class);
    private final FlowTaskletDbMapper flowTaskletDbMapper;

    public FlowTaskletRetryTasklet(FlowTaskletDbMapper flowTaskletDbMapper) {
        this.flowTaskletDbMapper = flowTaskletDbMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 실무형 예시:
        // RETRY 분기에 들어오면 분기 이력 테이블(TEST_FLOW_TASKLET_HISTORY)에 기록을 남깁니다.
        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();
        Long safeExecutionId = jobExecutionId == null ? -1L : jobExecutionId;
        String message = "retry branch executed. reason=route=RETRY or TEST_TASKLET count is 0";
        int inserted = flowTaskletDbMapper.insertFlowTaskletHistory(
                safeExecutionId,
                "flowTaskletRetryStep",
                "RETRY_PATH",
                message
        );
        log.info("[FLOW-TASKLET] retry path step executed. history insertedRows={}", inserted);
        return RepeatStatus.FINISHED;
    }
}
