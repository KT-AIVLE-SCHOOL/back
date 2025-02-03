package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.AdminInfo;

public interface AdminRepository extends JpaRepository<AdminInfo, Long> {
    AdminInfo findByAdminId(String adminId);
    AdminInfo findByAccessToken(String accessToken);
}
