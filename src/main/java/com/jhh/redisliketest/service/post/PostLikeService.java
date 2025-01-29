package com.jhh.redisliketest.service.post;

import com.jhh.redisliketest.repository.UserSentenceLikeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;
    private final UserSentenceLikeLogRepository userSentenceLikeLogRepository;

    public void incrementPostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";
        Object likeCount = valueOperations.get(key);

        if (likeCount == null) {
            likeCount = userSentenceLikeLogRepository.countLikesByPostId(Integer.parseInt(postId));
            if (likeCount == null) {
                likeCount = 0;
            }
            valueOperations.set(key, likeCount, Duration.ofMinutes(30));
        }

        valueOperations.increment(key, 1);
        redisTemplate.expire(key, Duration.ofMinutes(30));
    }

    public void decrementPostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";
        Object likeCount = valueOperations.get(key);

        if (likeCount == null) {
            likeCount = userSentenceLikeLogRepository.countLikesByPostId(Integer.parseInt(postId));
            if (likeCount == null) {
                likeCount = 0;
            }
            valueOperations.set(key, likeCount, Duration.ofMinutes(30));
        }

        valueOperations.increment(key, -1);
        redisTemplate.expire(key, Duration.ofMinutes(30));
    }

    public Integer getPostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";

        Object count = valueOperations.get(key);

        if (count == null) {
            Integer parsedPostId = Integer.parseInt(postId);
            Integer likeCount = userSentenceLikeLogRepository.countLikesByPostId(parsedPostId);
            if (likeCount == null) {
                likeCount = 0;
            }
            valueOperations.set(key, likeCount);
            return likeCount;
        }

        return ((Number) count).intValue();
    }
}
