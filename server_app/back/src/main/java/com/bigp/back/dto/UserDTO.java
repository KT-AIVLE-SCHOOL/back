package com.bigp.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

public class UserDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private int appId;
        private String email;
        private String password;
        private String accessToken;
        private String refreshToken;
        private Short joinMethods;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BabyInfo {
        private Date checkTime;
        private int emotion;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatInfo {
        private Date requestTime;
        private String request;
        private String response;
    }
}
