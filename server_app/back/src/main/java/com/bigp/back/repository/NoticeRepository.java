package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.NoticeInfo;

public interface NoticeRepository extends JpaRepository<NoticeInfo, Long>{

}
