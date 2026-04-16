package com.example.springbatch.batchChunk;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChunkBatchConfig {

    private static final Logger log = LoggerFactory.getLogger(ChunkBatchConfig.class);

    private final ChunkBatchMapper chunkBatchMapper;

    public ChunkBatchConfig(ChunkBatchMapper chunkBatchMapper) {
        this.chunkBatchMapper = chunkBatchMapper;
    }

    @PostConstruct
    public void initSchemaAndSeed() {
        chunkBatchMapper.createChunkSourceTableIfNotExists();
        chunkBatchMapper.createChunkTargetTableIfNotExists();

        if (chunkBatchMapper.countSource() == 0) {
            for (long i = 1; i <= 20; i++) {
                chunkBatchMapper.insertSeed(i, "source-" + i, "col2-" + i, "col3-" + i);
            }
        }
    }

    @Bean
    public Step chunkDbStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            ChunkItemReader chunkItemReader,
                            ChunkItemProcessor chunkItemProcessor,
                            ChunkItemWriter chunkItemWriter) {
        return new StepBuilder("chunkDbStep", jobRepository)
                .<ChunkItem, ChunkItem>chunk(5, transactionManager)
                .reader(chunkItemReader)
                .processor(chunkItemProcessor)
                .writer(chunkItemWriter)
                .build();
    }

    @Bean
    public Step chunkSelectStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkSelectStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int sourceTotal = chunkBatchMapper.countSource();
                    int sourceProcessed = chunkBatchMapper.countSourceProcessed();
                    int targetTotal = chunkBatchMapper.countTarget();
                    log.info("[CHUNK-SELECT-STEP] sourceTotal={}, sourceProcessed={}, targetTotal={}",
                            sourceTotal, sourceProcessed, targetTotal);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job chunkDbJob(JobRepository jobRepository, Step chunkDbStep, Step chunkSelectStep) {
        return new JobBuilder("chunkDbJob", jobRepository)
                .start(chunkDbStep)
                .next(chunkSelectStep)
                .build();
    }
}
