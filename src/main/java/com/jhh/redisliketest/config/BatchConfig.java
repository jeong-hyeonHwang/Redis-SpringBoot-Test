package com.jhh.redisliketest.config;

import com.jhh.redisliketest.entity.User;
import com.jhh.redisliketest.entity.UserDateSentence;
import com.jhh.redisliketest.entity.UserSentenceLikeLog;
import com.jhh.redisliketest.repository.UserDateSentenceRepository;
import com.jhh.redisliketest.repository.UserRepository;
import com.jhh.redisliketest.repository.UserSentenceLikeLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final UserDateSentenceRepository userDateSentenceRepository;
    private final UserSentenceLikeLogRepository userSentenceLikeLogRepository;


    @Bean
    public Job redisToDatabaseJob() {
        return new JobBuilder("redisToDatabaseJob", jobRepository)
                .start(redisToDatabaseStep())
                .build();
    }

    @Bean
    public Step redisToDatabaseStep() {
        return new StepBuilder("redisToDatabaseStep", jobRepository)
                .<UserSentenceLikeLog, UserSentenceLikeLog>chunk(100, transactionManager)
                .reader(redisItemReader())
                .processor(redisItemProcessor())
                .writer(redisItemWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .skipLimit(10)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public ItemReader<UserSentenceLikeLog> redisItemReader() {
        String pattern = "user:*:likes";
        Set<String> keys = redisTemplate.keys(pattern);
        List<UserSentenceLikeLog> logs = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                String userIdStr = key.split(":")[1];
                Integer userId;

                try {
                    userId = Integer.parseInt(userIdStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid user ID format: " + userIdStr);
                    continue;
                }

                // User 엔티티 조회
                User user = userRepository.findById(userId).orElse(null);
                if (user == null) {
                    System.err.println("User not found for ID: " + userId);
                    continue;
                }

                // Redis에서 해시 데이터 조회 (e.g., {"sentence1": true, "sentence2": false})
                Map<Object, Object> likeData = redisTemplate.opsForHash().entries(key);

                for (Map.Entry<Object, Object> entry : likeData.entrySet()) {
                    String sentenceIdStr = (String) entry.getKey();
                    Integer sentenceId = null;

                    try {
                        sentenceId = Integer.parseInt(sentenceIdStr);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid sentence ID format: " + sentenceIdStr);
                        continue;
                    }

                    // UserDateSentence 엔티티 조회
                    UserDateSentence userDateSentence = userDateSentenceRepository.findById(sentenceId).orElse(null);
                    if (userDateSentence == null) {
                        System.err.println("UserDateSentence not found for ID: " + sentenceId);
                        continue;
                    }

                    // UserSentenceLikeLog 생성 및 설정
                    UserSentenceLikeLog log = new UserSentenceLikeLog();
                    log.setUserSentenceLikeLogId(null); // Auto Increment
                    log.setUser(user);
                    log.setUserDateSentence(userDateSentence);
                    log.setLikeYn((Boolean) entry.getValue());

                    logs.add(log);
                }
            }
        }

        log.info("size is {}", logs.size());
        return new IteratorItemReader<>(logs);
    }


    @Bean
    public ItemProcessor<UserSentenceLikeLog, UserSentenceLikeLog> redisItemProcessor() {
        return log -> {
            // 필요한 처리 로직 추가
            return log;
        };
    }

    @Bean
    public ItemWriter<UserSentenceLikeLog> redisItemWriter() {
        return userSentenceLikeLogRepository::saveAll;
    }
}
