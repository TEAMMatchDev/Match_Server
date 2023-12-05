package com.example.matchbatch.job;

import com.example.matchbatch.service.OrderService;
import com.example.matchbatch.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserDeleteJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserService userService;
    private static final String JOB_NAME = "유저 삭제";


    public Job deleteUserSchedulingJob() {
        log.info(JOB_NAME + " JOB START");

        return jobBuilderFactory.get(JOB_NAME)
                .start(deleteUserStep())
                .build();
    }

    private Step deleteUserStep() {
        return stepBuilderFactory.get(JOB_NAME+" STEP")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step!");
                    userService.deleteUserInfos();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
