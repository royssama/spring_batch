package com.example.springbatch.taskletFlatFileItemWriter;

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
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaskletFlatFileJobConfig {

    @Bean
    public FlatFileItemReader<FlatFileSampleItem> flatFileSampleReader() {
        return new FlatFileItemReaderBuilder<FlatFileSampleItem>()
                .name("flatFileSampleReader")
                .resource(new FileSystemResource("output/flatfile/sample-input.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "name", "score")
                .targetType(FlatFileSampleItem.class)
                .build();
    }

    @Bean
    public FlatFileItemWriter<FlatFileSampleItem> flatFileSampleWriter() {
        DelimitedLineAggregator<FlatFileSampleItem> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor((FieldExtractor<FlatFileSampleItem>) item -> new Object[]{
                item.getId(),
                item.getName(),
                item.getScore()
        });

        return new FlatFileItemWriterBuilder<FlatFileSampleItem>()
                .name("flatFileSampleWriter")
                .resource(new FileSystemResource("output/flatfile/sample-output.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id,name,score"))
                .shouldDeleteIfExists(true)
                .build();
    }

    @Bean
    public Step createFlatFileInputStep(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        CreateSampleInputTasklet createSampleInputTasklet) {
        return new StepBuilder("createFlatFileInputStep", jobRepository)
                .tasklet(createSampleInputTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step flatFileReadWriteStep(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager,
                                      FlatFileItemReader<FlatFileSampleItem> flatFileSampleReader,
                                      FlatFileItemWriter<FlatFileSampleItem> flatFileSampleWriter) {
        return new StepBuilder("flatFileReadWriteStep", jobRepository)
                .<FlatFileSampleItem, FlatFileSampleItem>chunk(2, transactionManager)
                .reader(flatFileSampleReader)
                .processor(item -> {
                    item.setName(item.getName().toUpperCase());
                    item.setScore(item.getScore() + 5);
                    return item;
                })
                .writer(flatFileSampleWriter)
                .build();
    }

    @Bean
    public Job taskletFlatFileSampleJob(JobRepository jobRepository,
                                        Step createFlatFileInputStep,
                                        Step flatFileReadWriteStep) {
        return new JobBuilder("taskletFlatFileSampleJob", jobRepository)
                .start(createFlatFileInputStep)
                .next(flatFileReadWriteStep)
                .build();
    }
}
