package com.bigp.back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BabyEmotion {
    @Id
    @GeneratedValue
    private Long id;

    private String checkTime;
    private int emotion;

    @ManyToOne
    @JoinColumn(name="baby_id")
    @JsonBackReference
    BabyInfo baby;
}
