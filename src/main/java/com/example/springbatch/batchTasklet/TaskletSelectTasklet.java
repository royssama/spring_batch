package com.example.springbatch.batchTasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskletSelectTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(TaskletSelectTasklet.class);

    private final TaskletBatchMapper taskletBatchMapper;

    public TaskletSelectTasklet(TaskletBatchMapper taskletBatchMapper) {
        this.taskletBatchMapper = taskletBatchMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int total = taskletBatchMapper.countTaskletLogs();
        log.info("[TASKLET-SELECT-STEP] TEST_TASKLET totalRows={}", total);
        return RepeatStatus.FINISHED;
    }
}
