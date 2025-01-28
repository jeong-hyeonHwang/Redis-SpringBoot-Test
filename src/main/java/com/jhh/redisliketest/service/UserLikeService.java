package com.jhh.redisliketest.service;

import com.jhh.redisliketest.entity.UserSentenceLikeLog;
import com.jhh.redisliketest.repository.UserSentenceLikeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Boolean> hashOperations;
    private final UserSentenceLikeLogRepository userSentenceLikeLogRepository;

    public void setUserLike(String userId, String postId, Boolean like) {
        String key = "user:" + userId + ":likes";
        hashOperations.put(key, postId, like);
        redisTemplate.expire(key, Duration.ofDays(1));
    }

    public Boolean getUserLike(String userId, String postId) {
        String key = "user:" + userId + ":likes";

        Boolean like = hashOperations.get(key, postId);

        if (like == null) {
            Integer parsedUserId = Integer.parseInt(userId);
            Integer parsedPostId = Integer.parseInt(postId);

            Optional<UserSentenceLikeLog> optionalLog = userSentenceLikeLogRepository
                    .findByUser_UserIdAndUserDateSentence_UserDateSentenceId(parsedUserId, parsedPostId);
            if(optionalLog.isPresent()) {
                like = optionalLog.get().getLikeYn();
            }
            hashOperations.put(key, postId, like);
        } else {
            like = false;
            hashOperations.put(key, postId, like);
        }
        return hashOperations.get(key, postId);
    }

    public Map<String, Boolean> getAllLikesForUser(String userId) {
        String key = "user:" + userId + "likes";

        Map<String, Boolean> likes = hashOperations.entries(key);

        if (likes.isEmpty()) {
            Integer parsedUserId = Integer.parseInt(userId);
            List<UserSentenceLikeLog> likeLogs = userSentenceLikeLogRepository.findByUser_UserId(parsedUserId);
            if (likeLogs != null && !likeLogs.isEmpty()) {
                likes = new HashMap<>();
                for (UserSentenceLikeLog log : likeLogs) {
                    String postId = log.getUserDateSentence().getUserDateSentenceId().toString();
                    Boolean likeYn = log.getLikeYn();
                    likes.put(postId, likeYn);
                }
                hashOperations.putAll(key, likes);
            } else {
                likes = Collections.emptyMap();
            }
        }

        return likes;
    }

//    public void removeUserLike(String userId, String postId) {
//        String key = "user:" + userId + ":likes";
//        hashOperations.delete(key, postId);
//    }
}
