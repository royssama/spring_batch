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

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // DB 조회값이 1~4건일 때 성공 분기로 들어오는 예시입니다.
        // (설정상 SUCCESS_PATH 또는 fallback '*' 매칭 시 이 step 실행)
        log.info("[FLOW-TASKLET] success branch step executed (example: 1<=dbCount<5).");
        return RepeatStatus.FINISHED;
    }
}
