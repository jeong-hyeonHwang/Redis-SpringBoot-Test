package com.jhh.redisliketest.repository;

import com.jhh.redisliketest.entity.UserSentenceLikeLog;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSentenceLikeLogRepository extends JpaRepository<UserSentenceLikeLog, Integer> {

    Optional<UserSentenceLikeLog> findByUser_UserIdAndUserDateSentence_UserDateSentenceId(Integer userId, Integer postId);
    List<UserSentenceLikeLog> findByUser_UserId(Integer userId);

    @Query("SELECT COUNT(u) " +
            "FROM UserSentenceLikeLog u " +
            "WHERE u.likeYn = true " +
            "AND u.userDateSentence.userDateSentenceId = :postId " +
            "AND u.createAt = (" +
            "  SELECT MAX(sub.createAt) " +
            "  FROM UserSentenceLikeLog sub " +
            "  WHERE sub.user.userId = u.user.userId " +
            "  AND sub.userDateSentence.userDateSentenceId = u.userDateSentence.userDateSentenceId" +
            ")")
    Integer countLikesByPostId(@Param("postId") Integer postId);
}
