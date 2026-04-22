package com.example.springbatch.flowJobBatchTasklet;

import com.example.springbatch.batchTasklet.TaskletBatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowTaskletRouteTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowTaskletRouteTasklet.class);
    private final TaskletBatchMapper taskletBatchMapper;

    public FlowTaskletRouteTasklet(TaskletBatchMapper taskletBatchMapper) {
        this.taskletBatchMapper = taskletBatchMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 초보자용 설명:
        // 1) 우선 job 파라미터(route)가 있으면 수동 분기를 사용합니다.
        // - route=RETRY -> RETRY_PATH
        // - route=STOP  -> STOP_PATH
        // 2) route가 없으면 DB(TEST_TASKLET) 조회 결과(count)로 분기합니다.
        // - count == 0        -> RETRY_PATH
        // - 1 <= count < 5    -> SUCCESS_PATH
        // - count >= 5        -> STOP_PATH
        Object routeValue = chunkContext.getStepContext().getJobParameters().get("route");
        String route = routeValue == null ? "" : routeValue.toString();

        if ("RETRY".equalsIgnoreCase(route)) {
            contribution.setExitStatus(new ExitStatus("RETRY_PATH"));
            log.info("[FLOW-TASKLET] route=RETRY -> ExitStatus=RETRY_PATH");
            return RepeatStatus.FINISHED;
        }

        if ("STOP".equalsIgnoreCase(route)) {
            contribution.setExitStatus(new ExitStatus("STOP_PATH"));
            log.info("[FLOW-TASKLET] route=STOP -> ExitStatus=STOP_PATH");
            return RepeatStatus.FINISHED;
        }

        int logCount = taskletBatchMapper.countTaskletLogs();

        if (logCount == 0) {
            contribution.setExitStatus(new ExitStatus("RETRY_PATH"));
            log.info("[FLOW-TASKLET] dbCount={} -> ExitStatus=RETRY_PATH", logCount);
            return RepeatStatus.FINISHED;
        }

        if (logCount >= 5) {
            contribution.setExitStatus(new ExitStatus("STOP_PATH"));
            log.info("[FLOW-TASKLET] dbCount={} -> ExitStatus=STOP_PATH", logCount);
            return RepeatStatus.FINISHED;
        }

        contribution.setExitStatus(new ExitStatus("SUCCESS_PATH"));
        log.info("[FLOW-TASKLET] dbCount={} -> ExitStatus=SUCCESS_PATH", logCount);
        return RepeatStatus.FINISHED;
    }
}
