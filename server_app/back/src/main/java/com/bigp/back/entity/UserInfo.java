package com.bigp.back.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 사용자명
    private String username;
    // 사용자 암호
    private String password;
    // 사용자 이메일
    private String email;
    // jwt 토큰
    private String accessToken;
    private String refreshToken;
    private String aliasname;
    
    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] profileImage;

    @OneToOne(fetch=FetchType.LAZY)
    @JsonManagedReference
    private BabyInfo babyInfo;

    @OneToOne(fetch=FetchType.LAZY)
    @JsonManagedReference
    private ConfigInfo configInfo;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="chat")
    @JsonManagedReference
    private List<ChatInfo> chatInfoList;
}
