package com.example.matchbatch.scheduler;

import com.example.matchbatch.job.UserDeleteJob;
import com.example.matchbatch.service.UserService;
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
public class UserDeleteScheduler {
    private final JobLauncher jobLauncher;
    private final UserDeleteJob userDeleteJob;

    @Scheduled(cron = "0 30 3 * * *")
    public void deleteUserSchedule(){
        log.info("유저 삭제 정기 스케줄러 시작");
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(userDeleteJob.deleteUserSchedulingJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
