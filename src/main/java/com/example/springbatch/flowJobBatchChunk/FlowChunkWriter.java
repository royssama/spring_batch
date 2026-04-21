package com.example.springbatch.flowJobBatchChunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FlowChunkWriter implements ItemWriter<FlowChunkItem> {

    private static final Logger log = LoggerFactory.getLogger(FlowChunkWriter.class);

    @Override
    public void write(Chunk<? extends FlowChunkItem> items) {
        // 초보자용 설명:
        // writer는 가공된 데이터를 최종 저장하는 단계입니다.
        // 여기서는 DB 대신 로그 출력으로 예제를 단순화했습니다.
        for (FlowChunkItem item : items) {
            log.info("[FLOW-CHUNK] writing item id={}, sourceValue={}, processedValue={}",
                    item.getId(), item.getSourceValue(), item.getProcessedValue());
        }
    }
}
