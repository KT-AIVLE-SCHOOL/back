package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.BabyEmotion;

public interface BabyEmotionRepository extends JpaRepository<BabyEmotion, Long> {
}
