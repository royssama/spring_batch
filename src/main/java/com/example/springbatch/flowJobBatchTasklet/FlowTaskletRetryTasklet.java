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

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // route가 RETRY일 때 들어오는 분기 step 예시입니다.
        log.info("[FLOW-TASKLET] retry path step executed");
        return RepeatStatus.FINISHED;
    }
}
