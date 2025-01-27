package com.jhh.redisliketest.controller;

import com.jhh.redisliketest.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post-likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Autowired
    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/increment")
    public void incrementLike(@RequestParam String postId) {
        postLikeService.incrementPostLikeCount(postId);
    }

    @PostMapping("/decrement")
    public void decrementLike(@RequestParam String postId) {
        postLikeService.decrementPostLikeCount(postId);
    }

    @GetMapping("/count")
    public Long getLikeCount(@RequestParam String postId) {
        return postLikeService.getPostLikeCount(postId);
    }

    @PostMapping("/set-count")
    public void setLikeCount(@RequestParam String postId, @RequestParam Long count) {
        postLikeService.setPostLikeCount(postId, count);
    }

    @DeleteMapping("/delete-count")
    public void deleteLikeCount(@RequestParam String postId) {
        postLikeService.deletePostLikeCount(postId);
    }
}
