package com.example.springbatch.flowJobBatchChunk;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FlowChunkProcessor implements ItemProcessor<FlowChunkItem, FlowChunkItem> {

    @Override
    public FlowChunkItem process(FlowChunkItem item) {
        // 초보자용 설명:
        // chunk의 processor는 "읽은 데이터"를 "가공"하는 단계입니다.
        // 여기서는 sourceValue를 대문자로 만들고,
        // ID가 홀수면 SUCCESS, 짝수면 WARN 접두어를 붙입니다.
        String source = item.getSourceValue() == null ? "" : item.getSourceValue().toUpperCase();
        String prefix = item.getId() != null && item.getId() % 2 == 0 ? "WARN" : "SUCCESS";
        item.setProcessedValue(prefix + "-" + source);
        return item;
    }
}
