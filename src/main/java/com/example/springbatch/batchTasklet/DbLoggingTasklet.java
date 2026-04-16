package com.example.springbatch.batchTasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DbLoggingTasklet implements Tasklet {

    private final TaskletBatchMapper taskletBatchMapper;

    public DbLoggingTasklet(TaskletBatchMapper taskletBatchMapper) {
        this.taskletBatchMapper = taskletBatchMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String message = "Tasklet executed at " + LocalDateTime.now();
        taskletBatchMapper.insertTaskletLog(message);
        return RepeatStatus.FINISHED;
    }
}
