package com.example.springbatch.flowJobBatchChunk;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlowChunkReader implements ItemReader<FlowChunkItem> {

    private final List<FlowChunkItem> items = new ArrayList<>();
    private int index = 0;

    public FlowChunkReader() {
        // 초보자용 설명:
        // DB 대신 메모리 데이터로 간단히 예제를 구성했습니다.
        // 홀수 ID는 SUCCESS, 짝수 ID는 WARN 상태로 가공할 예정입니다.
        for (int i = 1; i <= 6; i++) {
            items.add(new FlowChunkItem((long) i, "item-" + i));
        }
    }

    @Override
    public FlowChunkItem read() {
        if (index >= items.size()) {
            // 다음 Job 실행 때 다시 처음부터 읽을 수 있도록 인덱스를 초기화합니다.
            index = 0;
            return null; // null을 반환하면 Chunk Step이 종료됩니다.
        }
        return items.get(index++);
    }
}
