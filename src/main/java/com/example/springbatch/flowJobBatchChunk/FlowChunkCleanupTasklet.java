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
        // DB 조회 결과상 아직 처리할 데이터가 없을 때(예: 미처리 건수 0) 들어오는 분기입니다.
        // 이 분기에서는 chunk step을 건너뛰고 종료성 정리 로그만 남깁니다.
        log.info("[FLOW-CHUNK] cleanup branch step executed. no pending source rows.");
        return RepeatStatus.FINISHED;
    }
}
