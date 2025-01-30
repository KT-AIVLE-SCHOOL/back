package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.ConfigInfo;

public interface ConfigRepository extends JpaRepository<ConfigInfo, Long> {
}
