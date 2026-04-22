package com.example.springbatch.flowJobBatchTasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowTaskletSuccessTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowTaskletSuccessTasklet.class);
    private final FlowTaskletDbMapper flowTaskletDbMapper;

    public FlowTaskletSuccessTasklet(FlowTaskletDbMapper flowTaskletDbMapper) {
        this.flowTaskletDbMapper = flowTaskletDbMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 실무형 예제:
        // 성공 분기로 들어오면 이력 테이블(TEST_FLOW_TASKLET_HISTORY)에 SUCCESS 결과를 남깁니다.
        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();
        int historyCount = flowTaskletDbMapper.countFlowTaskletHistory();
        String detail = "success branch completed. existingHistoryCount=" + historyCount;
        flowTaskletDbMapper.insertFlowTaskletHistory(
                safeExecutionId(jobExecutionId),
                "flowTaskletSuccessStep",
                "SUCCESS_PATH",
                detail
        );

        log.info("[FLOW-TASKLET] success path recorded. jobExecutionId={}, detail={}", jobExecutionId, detail);
        return RepeatStatus.FINISHED;
    }

    private Long safeExecutionId(Long jobExecutionId) {
        return jobExecutionId == null ? -1L : jobExecutionId;
    }
}
