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
        // DB 조회 분기에서 SUMMARY_PATH(또는 fallback '*')로 들어왔을 때 실행됩니다.
        // 예시 기준: sourceTotal > 0 이고, 미처리 건수가 0건일 때 요약 경로로 분기합니다.
        log.info("[FLOW-CHUNK] summary step executed (example: db says no unprocessed rows).");
        return RepeatStatus.FINISHED;
    }
}
