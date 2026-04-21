package com.example.springbatch.flowJobBatchChunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FlowChunkRouteTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(FlowChunkRouteTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 초보자용 설명:
        // chunk Flow Job에서 첫 state(step)로 분기 키를 정합니다.
        // - route=SKIP_CHUNK 이면 chunk 처리 step을 건너뜁니다.
        // - 그 외에는 chunk step으로 진행합니다.
        Object routeValue = chunkContext.getStepContext().getJobParameters().get("route");
        String route = routeValue == null ? "" : routeValue.toString();

        if ("SKIP_CHUNK".equalsIgnoreCase(route)) {
            contribution.setExitStatus(new ExitStatus("SKIP_PATH"));
            log.info("[FLOW-CHUNK] route=SKIP_CHUNK -> ExitStatus=SKIP_PATH");
        } else {
            contribution.setExitStatus(new ExitStatus("PROCESS_PATH"));
            log.info("[FLOW-CHUNK] route={} -> ExitStatus=PROCESS_PATH", route);
        }

        return RepeatStatus.FINISHED;
    }
}
