package com.example.springbatch.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestBatchMapper {

    void createTestTableIfNotExists();

    Long nextId();

    int insertRow(@Param("id") Long id,
                  @Param("col1") String col1,
                  @Param("col2") String col2,
                  @Param("col3") String col3);

    int updateCol2();

    int updateCol3();

    int countRows();

    int deleteAll();
}
