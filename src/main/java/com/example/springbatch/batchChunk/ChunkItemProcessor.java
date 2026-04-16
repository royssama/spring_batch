package com.example.springbatch.batchChunk;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ChunkItemProcessor implements ItemProcessor<ChunkItem, ChunkItem> {

    @Override
    public ChunkItem process(ChunkItem item) {
        item.setCol1(item.getCol1() == null ? null : item.getCol1().toUpperCase());
        item.setCol2("PROCESSED-" + item.getCol2());
        return item;
    }
}
