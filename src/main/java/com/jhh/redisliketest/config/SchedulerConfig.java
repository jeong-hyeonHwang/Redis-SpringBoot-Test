package com.jhh.redisliketest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final Job redisToDatabaseJob;

    @Scheduled(cron = "0 */5 * * * ?")
    public void runBatchJob() {
        try {
            jobLauncher.run(redisToDatabaseJob, new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
            log.info("Batch job executed successfully.");
        } catch (Exception e) {
            log.error("Failed to executed batch job {}", e.getMessage());
        }
    }
}
