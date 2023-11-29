package com.example.matchbatch.job;

import com.example.matchbatch.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class DonationRegularPayment { // Job 정의

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrderService orderService;
    private static final String JOB_NAME = "정기 구독(기부) 결제";


    public Job regularPaymentJob() {
        log.info(JOB_NAME + " JOB START");

        return jobBuilderFactory.get(JOB_NAME)
                .start(regularPaymentStep())
                .build();
    }

    private Step regularPaymentStep() {
        return stepBuilderFactory.get(JOB_NAME+" STEP")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step!");
                    orderService.regularDonationPayment();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
