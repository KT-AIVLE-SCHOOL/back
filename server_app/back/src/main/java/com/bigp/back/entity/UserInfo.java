package com.bigp.back.entity;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String username;
    private String password;
    @Column(unique=true)
    private String email;
    private String accessToken;
    private String refreshToken;
    private String aliasname;
    
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(columnDefinition="bytea")
    private byte[] profileImage;

    @OneToOne(fetch=FetchType.LAZY)
    @JsonManagedReference
    private BabyInfo babyInfo;

    @OneToOne(fetch=FetchType.LAZY)
    @JsonManagedReference
    private ConfigInfo configInfo;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="chat", fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<ChatInfo> chatInfoList;
}
