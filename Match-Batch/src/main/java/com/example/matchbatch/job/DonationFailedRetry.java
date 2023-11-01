package com.example.matchbatch.job;

import com.example.matchbatch.service.OrderService;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DonationFailedRetry {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RegularPaymentRepository regularPaymentRepository;
    private final OrderService orderService;
    private static final String JOB_NAME = "정기 구독(기부) 결제 재시도";


    @Bean
    public Job regularPaymentRetryJob() {
        log.info(JOB_NAME + " job start");

        return jobBuilderFactory.get(JOB_NAME)
                .start(regularPaymentRetryStep())
                .build();
    }

    private Step regularPaymentRetryStep() {
        return stepBuilderFactory.get(JOB_NAME+"step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step!");
                    orderService.regularPaymentRetry();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
