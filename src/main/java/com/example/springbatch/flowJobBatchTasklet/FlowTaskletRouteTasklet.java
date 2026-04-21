package com.example.springbatch.flowJobBatchTasklet;

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

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 초보자용 설명:
        // job 실행 시 파라미터로 route 값을 주면 flow 분기 경로를 바꿀 수 있습니다.
        // - route=RETRY -> RETRY_PATH
        // - route=STOP  -> STOP_PATH
        // - 그 외/미입력 -> 기본 COMPLETED 경로
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

        // 별도 ExitStatus를 지정하지 않으면 기본값(COMPLETED)으로 처리됩니다.
        log.info("[FLOW-TASKLET] route={} -> ExitStatus=COMPLETED(default)", route);
        return RepeatStatus.FINISHED;
    }
}
