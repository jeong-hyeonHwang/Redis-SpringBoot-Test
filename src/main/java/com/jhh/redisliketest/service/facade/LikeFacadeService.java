package com.jhh.redisliketest.service.facade;

import com.jhh.redisliketest.service.post.PostLikeService;
import com.jhh.redisliketest.service.user.UserLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeFacadeService {

    private final UserLikeService userLikeService;
    private final PostLikeService postLikeService;

    @Transactional
    public void likePost(String userId, String postId) {
        userLikeService.setUserLike(userId, postId, true);
        postLikeService.incrementPostLikeCount(postId);
    }

    @Transactional
    public void unlikePost(String userId, String postId) {
        userLikeService.setUserLike(userId, postId, false);
        postLikeService.decrementPostLikeCount(postId);
    }

    public Integer getPostLikeCount(String postId) {
        return postLikeService.getPostLikeCount(postId);
    }

    public Boolean getUserLike(String userId, String postId) {
        return userLikeService.getUserLike(userId, postId);
    }

    public Map<String, Boolean> getAllLikesForUser(String userId) {
        return userLikeService.getAllLikesForUser(userId);
    }
}
