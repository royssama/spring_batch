package com.example.springbatch.taskletApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Tasklet API", description = "Run tasklet job from another screen API")
@RestController
@RequestMapping("/api/tasklet")
public class TaskletApiController {

    private final TaskletApiService taskletApiService;

    public TaskletApiController(TaskletApiService taskletApiService) {
        this.taskletApiService = taskletApiService;
    }

    @Operation(summary = "Run tasklet job by external screen request")
    @PostMapping("/run")
    public TaskletRunResponse runTasklet(@RequestBody(required = false) TaskletRunRequest request) throws Exception {
        return taskletApiService.runTaskletJob(request);
    }
}
