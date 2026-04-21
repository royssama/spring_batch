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
        // route가 RETRY/STOP이 아닐 때(패턴 '*') 도착하는 성공 분기 Step입니다.
        log.info("[FLOW-TASKLET] success branch step executed.");
        return RepeatStatus.FINISHED;
    }
}
