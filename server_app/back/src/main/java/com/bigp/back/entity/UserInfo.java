package com.bigp.back.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private int appId;
    // 사용자 이메일
    private String email;
    // social일 때는 access token, 기본 가입일 때는 password
    private String password;
    // 0: 기본 가입, 1: kakao, 2: naver
    private Short joinMethods;
    // jwt 토큰
    private String accessToken;
    private String refreshToken;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="baby")
    @JsonManagedReference
    private List<BabyInfo> babyInfoList;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="chat")
    @JsonManagedReference
    private List<ChatInfo> chatInfoList;
}
