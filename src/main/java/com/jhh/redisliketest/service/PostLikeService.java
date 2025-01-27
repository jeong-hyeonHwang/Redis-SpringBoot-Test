package com.jhh.redisliketest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;

    @Autowired
    public PostLikeService(RedisTemplate<String, Object> redisTemplate, ValueOperations<String, Object> valueOperations) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = valueOperations;
    }

    public void incrementPostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";
        valueOperations.increment(key, 1);
    }

    public void decrementPostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";
        valueOperations.increment(key, -1);
    }

    public Long getPostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";
        Object count = valueOperations.get(key);
        return count != null ? ( (Number) count).longValue() : 0L;
    }

    public void setPostLikeCount(String postId, Long count) {
        String key = "post:" + postId + ":likeCount";
        valueOperations.set(key, -1);
    }

    public void deletePostLikeCount(String postId) {
        String key = "post:" + postId + ":likeCount";
        redisTemplate.delete(key);
    }
}
