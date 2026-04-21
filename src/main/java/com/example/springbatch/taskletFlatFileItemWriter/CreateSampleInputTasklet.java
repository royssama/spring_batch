package com.example.springbatch.taskletFlatFileItemWriter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class CreateSampleInputTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path inputDir = Path.of("output", "flatfile");
        Files.createDirectories(inputDir);

        Path inputFile = inputDir.resolve("sample-input.csv");
        List<String> lines = List.of(
                "id,name,score",
                "1,kim,90",
                "2,lee,80",
                "3,park,70",
                "4,choi,60"
        );
        Files.write(inputFile, lines, StandardCharsets.UTF_8);
        return RepeatStatus.FINISHED;
    }
}
