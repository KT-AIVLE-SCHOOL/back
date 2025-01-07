package com.bigp.back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BabyInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 아기 음성 검사 시간
    private Date checkTime;
    // 아기 감정
    private int emotion;

    @ManyToOne
    @JoinColumn(name="baby_id")
    @JsonBackReference
    private UserInfo baby;
}
