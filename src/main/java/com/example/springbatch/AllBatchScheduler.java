package com.example.springbatch;

import com.example.springbatch.batch.TestBatchScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AllBatchScheduler {

    private static final Logger log = LoggerFactory.getLogger(AllBatchScheduler.class);

    private final TestBatchScheduler testBatchScheduler;
    private final JobLauncher jobLauncher;
    private final Job taskletDbJob;
    private final Job chunkDbJob;
    private final Job taskletApiCallJob;
    private final Job flowTaskletJob;
    private final Job flowChunkJob;

    public AllBatchScheduler(TestBatchScheduler testBatchScheduler,
                             JobLauncher jobLauncher,
                             @Qualifier("taskletDbJob") Job taskletDbJob,
                             @Qualifier("chunkDbJob") Job chunkDbJob,
                             @Qualifier("taskletApiCallJob") Job taskletApiCallJob,
                             @Qualifier("flowTaskletJob") Job flowTaskletJob,
                             @Qualifier("flowChunkJob") Job flowChunkJob) {
        this.testBatchScheduler = testBatchScheduler;
        this.jobLauncher = jobLauncher;
        this.taskletDbJob = taskletDbJob;
        this.chunkDbJob = chunkDbJob;
        this.taskletApiCallJob = taskletApiCallJob;
        this.flowTaskletJob = flowTaskletJob;
        this.flowChunkJob = flowChunkJob;
    }

    // -----------------------------
    // 기존 배치 방식 (src/main/java/com/example/springbatch/batch)
    // -----------------------------
    @Scheduled(cron = "${app.batch.general.insert-cron:*/5 * * * * *}")
    public void runGeneralInsert() {
        log.info("1: runGeneralInsert");
      //  testBatchScheduler.insertEveryMinute();
    }

    @Scheduled(cron = "${app.batch.general.update5s-cron:*/10 * * * * *}")
    public void runGeneralUpdate5s() {
        log.info("2: runGeneralUpdate5s");
     //   testBatchScheduler.updateEvery5Seconds();
    }

    @Scheduled(cron = "${app.batch.general.update10s-cron:*/5 * * * * *}")
    public void runGeneralUpdate10s() {
        log.info("3: runGeneralUpdate10s");
     //   testBatchScheduler.updateEvery10Seconds();
    }

    @Scheduled(cron = "${app.batch.general.select15s-cron:*/20 * * * * *}")
    public void runGeneralSelect15s() {
        log.info("4: runGeneralSelect15s");
      //  testBatchScheduler.selectEvery15Seconds();
    }

    @Scheduled(cron = "${app.batch.general.delete-cron:*/25 * * * * *}")
    public void runGeneralDelete() {
        log.info("5: runGeneralDelete");
      //  testBatchScheduler.deleteAllEveryMinute();
    }

    // -----------------------------
    // batchTasklet 방식 (src/main/java/com/example/springbatch/batchTasklet)
    // -----------------------------
    @Scheduled(cron = "${app.batch.tasklet.cron:*/5 * * * * *}")
    public void runTaskletJob() {
        log.info("6: runTaskletJob");
        runJob(taskletDbJob, "taskletDbJob");
    }

    // -----------------------------
    // batchChunk 방식 (src/main/java/com/example/springbatch/batchChunk)
    // -----------------------------
    @Scheduled(cron = "${app.batch.chunk.cron:*/5 * * * * *}")
    public void runChunkJob() {
        log.info("7: runChunkJob");
        runJob(chunkDbJob, "chunkDbJob");
    }

    // -----------------------------
    // taskletApi 방식 (src/main/java/com/example/springbatch/taskletApi)
    // -----------------------------
    @Scheduled(cron = "${app.batch.tasklet-api.cron:*/40 * * * * *}")
    public void runTaskletApiJob() {
        log.info("8: runTaskletApiJob");
        runJob(taskletApiCallJob, "taskletApiCallJob");
    }

    // -----------------------------
    // flowJobBatchTasklet 방식 (src/main/java/com/example/springbatch/flowJobBatchTasklet)
    // -----------------------------
    @Scheduled(cron = "${app.batch.flow-tasklet.cron:*/45 * * * * *}")
    public void runFlowTaskletJob() {
        log.info("9: runFlowTaskletJob");
        runJob(flowTaskletJob, "flowTaskletJob");
    }

    // -----------------------------
    // flowJobBatchChunk 방식 (src/main/java/com/example/springbatch/flowJobBatchChunk)
    // -----------------------------
    @Scheduled(cron = "${app.batch.flow-chunk.cron:*/50 * * * * *}")
    public void runFlowChunkJob() {
        log.info("10: runFlowChunkJob");
        runJob(flowChunkJob, "flowChunkJob");
    }

    private void runJob(Job job, String jobName) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(job, params);
            log.info("[{}-CRON] executionId={}, status={}", jobName, execution.getId(), execution.getStatus());
        } catch (Exception e) {
            log.error("[{}-CRON] failed", jobName, e);
        }
    }
}
