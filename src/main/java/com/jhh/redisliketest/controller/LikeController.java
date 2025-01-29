package com.jhh.redisliketest.controller;

import com.jhh.redisliketest.service.facade.LikeFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeFacadeService likeFacadeService;

    // 좋아요 추가
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable String postId, @RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        likeFacadeService.likePost(userId, postId);
        Integer likeCount = likeFacadeService.getPostLikeCount(postId);
        return ResponseEntity.ok(Map.of("message", "Post liked successfully.", "likeCount", likeCount));
    }

    // 좋아요 취소
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<?> unlikePost(@PathVariable String postId, @RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        likeFacadeService.unlikePost(userId, postId);
        Integer likeCount = likeFacadeService.getPostLikeCount(postId);
        return ResponseEntity.ok(Map.of("message", "Post unliked successfully.", "likeCount", likeCount));
    }

    // 게시물 좋아요 수 조회
    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable String postId) {
        Integer likeCount = likeFacadeService.getPostLikeCount(postId);
        return ResponseEntity.ok(Map.of("postId", postId, "likeCount", likeCount));
    }

    // 사용자 특정 게시물 좋아요 상태 조회
    @GetMapping("/users/{userId}/posts/{postId}/like")
    public ResponseEntity<?> getUserLike(@PathVariable String userId, @PathVariable String postId) {
        Boolean liked = likeFacadeService.getUserLike(userId, postId);
        return ResponseEntity.ok(Map.of("userId", userId, "postId", postId, "liked", liked));
    }

    // 사용자 모든 좋아요 상태 조회
    @GetMapping("/users/{userId}/likes")
    public ResponseEntity<?> getAllUserLikes(@PathVariable String userId) {
        Map<String, Boolean> likes = likeFacadeService.getAllLikesForUser(userId);
        return ResponseEntity.ok(Map.of("userId", userId, "likes", likes));
    }
}
