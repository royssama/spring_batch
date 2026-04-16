package com.example.springbatch.batchTasklet;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskletBatchMapper {

    void createTaskletTableIfNotExists();

    int insertTaskletLog(@Param("message") String message);

    int countTaskletLogs();
}
