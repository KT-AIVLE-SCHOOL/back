package com.bigp.back.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class ChatInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Date requestTime;

    @ElementCollection
    @CollectionTable(name = "chat_requests", joinColumns = @JoinColumn(name = "chat_info_id"))
    @Column(name = "request", columnDefinition = "TEXT")
    private List<String> request;

    @ElementCollection
    @CollectionTable(name = "chat_responses", joinColumns = @JoinColumn(name = "chat_info_id"))
    @Column(name = "response", columnDefinition = "TEXT")
    private List<String> response;

    @ManyToOne
    @JoinColumn(name="chat_id")
    @JsonBackReference
    private UserInfo chat;
}
