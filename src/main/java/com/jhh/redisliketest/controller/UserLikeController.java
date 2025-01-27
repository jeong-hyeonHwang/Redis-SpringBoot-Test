package com.jhh.redisliketest.controller;

import com.jhh.redisliketest.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-likes")
public class UserLikeController {

    private final UserLikeService userLikeService;

    @Autowired
    public UserLikeController(UserLikeService userLikeService) {
        this.userLikeService = userLikeService;
    }

    @PostMapping("/set-like")
    public void setUserLike(@RequestParam String userId, @RequestParam String postId, @RequestParam Boolean like) {
        userLikeService.setUserLike(userId, postId, like);
    }

    @GetMapping("/get-like")
    public Boolean getUserLike(@RequestParam String userId, @RequestParam String postId) {
        return userLikeService.getUserLike(userId, postId);
    }

    @GetMapping("/get-all-likes")
    public Map<String, Boolean> getAllLikesForUser(@RequestParam String userId) {
        return userLikeService.getAllLikesForUser(userId);
    }

    @DeleteMapping("/remove-like")
    public void removeUserLike(@RequestParam String userId, @RequestParam String postId) {
        userLikeService.removeUserLike(userId, postId);
    }
}
