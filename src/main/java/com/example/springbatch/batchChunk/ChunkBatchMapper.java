package com.example.springbatch.batchChunk;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChunkBatchMapper {

    void createChunkSourceTableIfNotExists();

    void createChunkTargetTableIfNotExists();

    int countSource();

    int countSourceProcessed();

    int countTarget();

    int insertSeed(@Param("id") Long id,
                   @Param("col1") String col1,
                   @Param("col2") String col2,
                   @Param("col3") String col3);

    List<ChunkItem> readPage(@Param("offset") int offset, @Param("size") int size);

    int insertTarget(ChunkItem item);

    int markProcessed(@Param("id") Long id);
}
