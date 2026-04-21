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

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // STOP 분기로 들어왔을 때 실행되는 Tasklet 예시입니다.
        log.info("[FLOW-TASKLET] STOP 분기 Step 실행");
        return RepeatStatus.FINISHED;
    }
}
