package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.BabyInfo;

public interface BabyRepository extends JpaRepository<BabyInfo, Long> {

}
