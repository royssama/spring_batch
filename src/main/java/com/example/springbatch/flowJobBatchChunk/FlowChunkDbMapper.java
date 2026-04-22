package com.example.springbatch.flowJobBatchChunk;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowChunkDbMapper {

    int countSource();

    int countSourceProcessed();

    int countUnprocessedSource();
}
