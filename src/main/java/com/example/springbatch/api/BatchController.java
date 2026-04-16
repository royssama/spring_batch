package com.example.springbatch.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Batch", description = "Spring Batch execution APIs")
@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job generalSampleJob;
    private final Job taskletDbJob;
    private final Job chunkDbJob;

    public BatchController(JobLauncher jobLauncher,
                           @Qualifier("generalSampleJob") Job generalSampleJob,
                           @Qualifier("taskletDbJob") Job taskletDbJob,
                           @Qualifier("chunkDbJob") Job chunkDbJob) {
        this.jobLauncher = jobLauncher;
        this.generalSampleJob = generalSampleJob;
        this.taskletDbJob = taskletDbJob;
        this.chunkDbJob = chunkDbJob;
    }

    @Operation(summary = "Run general sample batch job")
    @PostMapping("/run")
    public String runBatch() throws Exception {
        return runJob(generalSampleJob, "generalSampleJob");
    }

    @Operation(summary = "Run tasklet batch job")
    @PostMapping("/run/tasklet")
    public String runTaskletBatch() throws Exception {
        return runJob(taskletDbJob, "taskletDbJob");
    }

    @Operation(summary = "Run chunk batch job")
    @PostMapping("/run/chunk")
    public String runChunkBatch() throws Exception {
        return runJob(chunkDbJob, "chunkDbJob");
    }

    private String runJob(Job job, String jobName) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
        return jobName + " execution id=" + execution.getId() + ", status=" + execution.getStatus();
    }
}
