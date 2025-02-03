package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
    public VerificationCode findByEmail(String email);
}
