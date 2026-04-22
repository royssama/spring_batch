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
    private final FlowChunkDbMapper flowChunkDbMapper;

    public FlowChunkRouteTasklet(FlowChunkDbMapper flowChunkDbMapper) {
        this.flowChunkDbMapper = flowChunkDbMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // 초보자용 설명:
        // chunk Flow Job에서 첫 state(step)로 분기 키를 정합니다.
        // 1) route 파라미터가 있으면 수동 분기
        // - route=SKIP_CHUNK    -> SKIP_PATH
        // - route=PROCESS_CHUNK -> PROCESS_PATH
        // 2) route가 없으면 DB(TEST_CHUNK_SOURCE) 조회값으로 자동 분기
        // - sourceTotal == 0              -> SKIP_PATH
        // - sourceProcessed < sourceTotal -> PROCESS_PATH
        // - sourceProcessed == sourceTotal -> SUCCESS_PATH
        Object routeValue = chunkContext.getStepContext().getJobParameters().get("route");
        String route = routeValue == null ? "" : routeValue.toString();

        if ("SKIP_CHUNK".equalsIgnoreCase(route)) {
            contribution.setExitStatus(new ExitStatus("SKIP_PATH"));
            log.info("[FLOW-CHUNK] route=SKIP_CHUNK -> ExitStatus=SKIP_PATH");
            return RepeatStatus.FINISHED;
        }

        if ("PROCESS_CHUNK".equalsIgnoreCase(route)) {
            contribution.setExitStatus(new ExitStatus("PROCESS_PATH"));
            log.info("[FLOW-CHUNK] route=PROCESS_CHUNK -> ExitStatus=PROCESS_PATH");
            return RepeatStatus.FINISHED;
        }

        int sourceTotal = flowChunkDbMapper.countSource();
        int sourceProcessed = flowChunkDbMapper.countSourceProcessed();
        int unprocessed = flowChunkDbMapper.countUnprocessedSource();

        if (sourceTotal == 0) {
            contribution.setExitStatus(new ExitStatus("SKIP_PATH"));
            log.info("[FLOW-CHUNK] db sourceTotal={} -> ExitStatus=SKIP_PATH", sourceTotal);
            return RepeatStatus.FINISHED;
        }

        if (unprocessed > 0) {
            contribution.setExitStatus(new ExitStatus("PROCESS_PATH"));
            log.info("[FLOW-CHUNK] db sourceTotal={}, sourceProcessed={}, unprocessed={} -> ExitStatus=PROCESS_PATH",
                    sourceTotal, sourceProcessed, unprocessed);
            return RepeatStatus.FINISHED;
        }

        contribution.setExitStatus(new ExitStatus("SUMMARY_PATH"));
        log.info("[FLOW-CHUNK] db sourceTotal={}, sourceProcessed={}, unprocessed={} -> ExitStatus=SUMMARY_PATH",
                sourceTotal, sourceProcessed, unprocessed);
        return RepeatStatus.FINISHED;
    }
}
