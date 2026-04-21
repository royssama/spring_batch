package com.example.springbatch.taskletApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ExternalApiCallTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiCallTasklet.class);

    private final HttpClient httpClient;
    private final String endpointUrl;
    private final long readTimeoutMs;

    public ExternalApiCallTasklet(
            @Value("${app.batch.tasklet-api.endpoint-url:http://localhost:8081/api/batch/run/tasklet}") String endpointUrl,
            @Value("${app.batch.tasklet-api.connect-timeout-ms:3000}") long connectTimeoutMs,
            @Value("${app.batch.tasklet-api.read-timeout-ms:5000}") long readTimeoutMs
    ) {
        this.endpointUrl = endpointUrl;
        this.readTimeoutMs = readTimeoutMs;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .build();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();
        String payload = createPayload(jobExecutionId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .timeout(Duration.ofMillis(readTimeoutMs))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String body = shrink(response.body());

            if (statusCode < 200 || statusCode >= 300) {
                throw new IllegalStateException("External API call failed. status=" + statusCode + ", body=" + body);
            }

            log.info("[TASKLET-API] success. status={}, endpoint={}, body={}", statusCode, endpointUrl, body);
            return RepeatStatus.FINISHED;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("External API call interrupted. endpoint=" + endpointUrl, e);
        } catch (IOException e) {
            throw new IllegalStateException("External API call I/O failed. endpoint=" + endpointUrl, e);
        }
    }

    private String createPayload(Long jobExecutionId) {
        long safeExecutionId = jobExecutionId == null ? -1L : jobExecutionId;
        return String.format(
                "{\"source\":\"spring-batch\",\"triggeredAt\":\"%s\",\"jobExecutionId\":%d}",
                LocalDateTime.now(),
                safeExecutionId
        );
    }

    private String shrink(String body) {
        if (body == null || body.isBlank()) {
            return "";
        }

        int max = 200;
        return body.length() > max ? body.substring(0, max) + "..." : body;
    }
}
