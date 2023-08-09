package com.example.matchbatch.scheduler;

import com.example.matchbatch.job.DonationRegularPayment;
import com.example.matchcommon.annotation.Scheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Scheduler
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {
    private final JobLauncher jobLauncher;
    private final DonationRegularPayment regularPaymentJob;
    //매일 12시 30분에 실행하는 스케줄러
    @Scheduled(cron = "0 30 12 * * *")
    //매 1분마다 실행
    //@Scheduled(cron = "0,20,40 * * * * *", zone = "asia/seoul")
    //@Scheduled(fixedDelay = 10000)
    public void RegularPayScheduler(){
        log.info("정기 결제 스케줄러 시작");

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(regularPaymentJob.regularPaymentJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error(e.getMessage());
        }

    }
}
