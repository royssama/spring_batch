package com.example.springbatch.chunkFlatFileItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChunkFlatFileJobConfig {

    @Bean
    public FlatFileItemReader<ChunkFlatFileItem> chunkFlatFileReader() {
        return new FlatFileItemReaderBuilder<ChunkFlatFileItem>()
                .name("chunkFlatFileReader")
                .resource(new ClassPathResource("input/chunk-sample-input.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "name", "score")
                .targetType(ChunkFlatFileItem.class)
                .build();
    }

    @Bean
    public FlatFileItemWriter<ChunkFlatFileItem> chunkFlatFileWriter() {
        DelimitedLineAggregator<ChunkFlatFileItem> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor((FieldExtractor<ChunkFlatFileItem>) item -> new Object[]{
                item.getId(),
                item.getName(),
                item.getScore()
        });

        return new FlatFileItemWriterBuilder<ChunkFlatFileItem>()
                .name("chunkFlatFileWriter")
                .resource(new FileSystemResource("output/flatfile/chunk-output.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id,name,score"))
                .shouldDeleteIfExists(true)
                .build();
    }

    @Bean
    public Step chunkFlatFileStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  FlatFileItemReader<ChunkFlatFileItem> chunkFlatFileReader,
                                  FlatFileItemWriter<ChunkFlatFileItem> chunkFlatFileWriter) {
        return new StepBuilder("chunkFlatFileStep", jobRepository)
                .<ChunkFlatFileItem, ChunkFlatFileItem>chunk(2, transactionManager)
                .reader(chunkFlatFileReader)
                .processor(item -> {
                    item.setName(item.getName().toLowerCase());
                    item.setScore(item.getScore() + 10);
                    return item;
                })
                .writer(chunkFlatFileWriter)
                .build();
    }

    @Bean
    public Job chunkFlatFileSampleJob(JobRepository jobRepository, Step chunkFlatFileStep) {
        return new JobBuilder("chunkFlatFileSampleJob", jobRepository)
                .start(chunkFlatFileStep)
                .build();
    }
}
