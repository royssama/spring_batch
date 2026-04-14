package com.example.springbatch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SampleTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(SampleTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        log.info("Spring Batch sample task started at {}", LocalDateTime.now());
        return RepeatStatus.FINISHED;
    }
}
