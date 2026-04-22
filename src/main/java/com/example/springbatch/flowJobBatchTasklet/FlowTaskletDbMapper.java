package com.example.springbatch.flowJobBatchTasklet;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FlowTaskletDbMapper {

    void createFlowTaskletHistoryTableIfNotExists();

    int countFlowTaskletHistory();

    int insertFlowTaskletHistory(@Param("jobExecutionId") Long jobExecutionId,
                                 @Param("stepName") String stepName,
                                 @Param("branchCode") String branchCode,
                                 @Param("message") String message);
}
