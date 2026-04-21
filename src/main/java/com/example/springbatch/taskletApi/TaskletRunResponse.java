package com.example.springbatch.taskletApi;

public class TaskletRunResponse {

    private String jobName;
    private Long executionId;
    private String status;
    private String sourceScreenId;
    private String requestedBy;
    private String reason;

    public TaskletRunResponse() {
    }

    public TaskletRunResponse(String jobName,
                              Long executionId,
                              String status,
                              String sourceScreenId,
                              String requestedBy,
                              String reason) {
        this.jobName = jobName;
        this.executionId = executionId;
        this.status = status;
        this.sourceScreenId = sourceScreenId;
        this.requestedBy = requestedBy;
        this.reason = reason;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceScreenId() {
        return sourceScreenId;
    }

    public void setSourceScreenId(String sourceScreenId) {
        this.sourceScreenId = sourceScreenId;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
