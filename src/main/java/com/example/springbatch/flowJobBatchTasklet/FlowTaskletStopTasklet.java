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
        // DB 조회값이 5건 이상일 때 STOP_PATH로 들어오는 분기 예시입니다.
        log.info("[FLOW-TASKLET] stop path step executed (example: dbCount>=5)");
        return RepeatStatus.FINISHED;
    }
}
