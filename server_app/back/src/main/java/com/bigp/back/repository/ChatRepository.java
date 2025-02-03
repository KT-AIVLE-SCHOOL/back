package com.bigp.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigp.back.entity.ChatInfo;

public interface ChatRepository extends JpaRepository<ChatInfo, Long> {

}
