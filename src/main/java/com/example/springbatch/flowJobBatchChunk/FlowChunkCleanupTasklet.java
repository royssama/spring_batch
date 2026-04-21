package com.example.springbatch.flowJobBatchChunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowChunkCleanupTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowChunkCleanupTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // route=SKIP_CHUNK일 때 들어오는 대체 분기 Step입니다.
        log.info("[FLOW-CHUNK] cleanup branch step executed. chunk step was skipped.");
        return RepeatStatus.FINISHED;
    }
}
