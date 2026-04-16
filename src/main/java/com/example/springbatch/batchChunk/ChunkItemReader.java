package com.example.springbatch.batchChunk;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChunkItemReader implements ItemReader<ChunkItem> {

    private static final int PAGE_SIZE = 5;

    private final ChunkBatchMapper chunkBatchMapper;

    private int offset = 0;
    private int indexInPage = 0;
    private List<ChunkItem> currentPage = new ArrayList<>();

    public ChunkItemReader(ChunkBatchMapper chunkBatchMapper) {
        this.chunkBatchMapper = chunkBatchMapper;
    }

    @Override
    public ChunkItem read() {
        if (indexInPage >= currentPage.size()) {
            currentPage = chunkBatchMapper.readPage(offset, PAGE_SIZE);
            indexInPage = 0;
            if (currentPage.isEmpty()) {
                reset();
                return null;
            }
            offset += currentPage.size();
        }
        return currentPage.get(indexInPage++);
    }

    private void reset() {
        offset = 0;
        indexInPage = 0;
        currentPage = new ArrayList<>();
    }
}
