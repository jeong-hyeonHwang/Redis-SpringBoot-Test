package com.jhh.redisliketest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserLikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Boolean> hashOperations;

    @Autowired
    public UserLikeService(RedisTemplate<String, Object> redisTemplate, HashOperations<String, String, Boolean> hashOperations) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = hashOperations;
    }

    public void setUserLike(String userId, String postId, Boolean like) {
        String key = "user:" + userId + ":likes";
        hashOperations.put(key, postId, like);
    }

    public Boolean getUserLike(String userId, String postId) {
        String key = "user:" + userId + ":likes";
        return hashOperations.get(key, postId);
    }

    public Map<String, Boolean> getAllLikesForUser(String userId) {
        String key = "user:" + userId + "likes";
        return hashOperations.entries(key);
    }

    public void removeUserLike(String userId, String postId) {
        String key = "user:" + userId + ":likes";
        hashOperations.delete(key, postId);
    }
}
