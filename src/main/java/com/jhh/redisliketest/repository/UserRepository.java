package com.jhh.redisliketest.repository;

import com.jhh.redisliketest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
