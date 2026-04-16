package com.example.springbatch.batch;

import com.example.springbatch.mybatis.TestBatchMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestBatchScheduler {

    private static final Logger log = LoggerFactory.getLogger(TestBatchScheduler.class);

    private final TestBatchMapper testBatchMapper;

    public TestBatchScheduler(TestBatchMapper testBatchMapper) {
        this.testBatchMapper = testBatchMapper;
    }

    @PostConstruct
    public void initTable() {
        testBatchMapper.createTestTableIfNotExists();
        log.info("TEST table is ready.");
    }

    // default cron: 0 * * * * * (triggered by AllBatchScheduler)
    public void insertEveryMinute() {
        long id = testBatchMapper.nextId();
        String now = LocalDateTime.now().toString();
        int inserted = testBatchMapper.insertRow(id, "col1-" + id, "col2-" + now, "col3-" + now);
        log.info("[INSERT/1m] id={}, inserted={}", id, inserted);
    }

    // default cron: */5 * * * * * (triggered by AllBatchScheduler)
    public void updateEvery5Seconds() {
        int updated = testBatchMapper.updateCol2();
        log.info("[UPDATE/5s] updated={}", updated);
    }

    // default cron: */10 * * * * * (triggered by AllBatchScheduler)
    public void updateEvery10Seconds() {
        int updated = testBatchMapper.updateCol3();
        log.info("[UPDATE/10s] updated={}", updated);
    }

    // default cron: */15 * * * * * (triggered by AllBatchScheduler)
    public void selectEvery15Seconds() {
        int count = testBatchMapper.countRows();
        log.info("[SELECT/15s] totalRows={}", count);
    }

    // default cron: 30 * * * * * (triggered by AllBatchScheduler)
    public void deleteAllEveryMinute() {
        int deleted = testBatchMapper.deleteAll();
        log.info("[DELETE/1m] deleted={}", deleted);
    }
}
