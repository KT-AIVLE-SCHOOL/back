package com.bigp.back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    // chat 질문 시간
    private Date requestTime;
    // 질문
    private String request;
    // 응답
    private String response;

    @ManyToOne
    @JoinColumn(name="chat_id")
    @JsonBackReference
    private UserInfo chat;
}
