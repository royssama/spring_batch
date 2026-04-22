package com.example.springbatch.flowJobBatchTasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowTaskletStopTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowTaskletStopTasklet.class);
    private final FlowTaskletDbMapper flowTaskletDbMapper;

    public FlowTaskletStopTasklet(FlowTaskletDbMapper flowTaskletDbMapper) {
        this.flowTaskletDbMapper = flowTaskletDbMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 실무형 예제:
        // STOP 분기에 들어오면 "중단 사유"를 이력 테이블에 남깁니다.
        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();
        long safeJobExecutionId = jobExecutionId == null ? -1L : jobExecutionId;

        int insertedRows = flowTaskletDbMapper.insertFlowTaskletHistory(
                safeJobExecutionId,
                "flowTaskletStopStep",
                "STOP",
                "stop branch executed. reason=dbCount>=5 or route=STOP"
        );

        log.info("[FLOW-TASKLET] stop path step executed. jobExecutionId={}, insertedRows={}",
                safeJobExecutionId, insertedRows);
        return RepeatStatus.FINISHED;
    }
}
