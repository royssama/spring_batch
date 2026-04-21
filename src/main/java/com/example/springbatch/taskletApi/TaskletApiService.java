package com.example.springbatch.taskletApi;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TaskletApiService {

    private static final String TASKLET_JOB_NAME = "taskletDbJob";
    private static final String DEFAULT_SCREEN_ID = "UNKNOWN_SCREEN";
    private static final String DEFAULT_REQUESTED_BY = "SYSTEM";
    private static final String DEFAULT_REASON = "NO_REASON";

    private final JobLauncher jobLauncher;
    private final Job taskletDbJob;

    public TaskletApiService(JobLauncher jobLauncher,
                             @Qualifier("taskletDbJob") Job taskletDbJob) {
        this.jobLauncher = jobLauncher;
        this.taskletDbJob = taskletDbJob;
    }

    public TaskletRunResponse runTaskletJob(TaskletRunRequest request) throws Exception {
        String screenId = normalize(request != null ? request.getScreenId() : null, DEFAULT_SCREEN_ID);
        String requestedBy = normalize(request != null ? request.getRequestedBy() : null, DEFAULT_REQUESTED_BY);
        String reason = normalize(request != null ? request.getReason() : null, DEFAULT_REASON);

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("sourceScreenId", screenId)
                .addString("requestedBy", requestedBy)
                .addString("reason", reason)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(taskletDbJob, jobParameters);
        return new TaskletRunResponse(
                TASKLET_JOB_NAME,
                execution.getId(),
                execution.getStatus().toString(),
                screenId,
                requestedBy,
                reason
        );
    }

    private String normalize(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value.trim();
    }
}
