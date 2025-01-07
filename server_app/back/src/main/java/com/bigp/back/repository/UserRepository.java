package com.bigp.back.repository;

import com.bigp.back.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByAppId(int appId);
}
