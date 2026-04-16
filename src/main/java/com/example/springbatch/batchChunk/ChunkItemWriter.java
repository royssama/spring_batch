package com.example.springbatch.batchChunk;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class ChunkItemWriter implements ItemWriter<ChunkItem> {

    private final ChunkBatchMapper chunkBatchMapper;

    public ChunkItemWriter(ChunkBatchMapper chunkBatchMapper) {
        this.chunkBatchMapper = chunkBatchMapper;
    }

    @Override
    public void write(Chunk<? extends ChunkItem> items) {
        for (ChunkItem item : items) {
            chunkBatchMapper.insertTarget(item);
            chunkBatchMapper.markProcessed(item.getId());
        }
    }
}
