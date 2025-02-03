package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByEmail(String email);
    UserInfo findByAccessToken(String accessToken);
}
