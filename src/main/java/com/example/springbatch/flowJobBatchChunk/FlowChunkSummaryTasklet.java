package com.example.springbatch.flowJobBatchChunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowChunkSummaryTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowChunkSummaryTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 정상 분기(CHUNK_PATH)에서 chunk Step 이후 요약 로그를 남기는 예시입니다.
        log.info("[FLOW-CHUNK] summary step executed after chunk path.");
        return RepeatStatus.FINISHED;
    }
}
