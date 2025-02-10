package com.bigp.back.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private String username;
        private String email;
        private String password;
        private String aliasname;
        private String profileImage;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterInfo {
        private String username;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BabyInfo {
        private String babyname;
        private String babybirth;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BabyEmotion {
        private Date checkTime;
        private int emotion;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatInfo {
        private Date requestTime;
        private List<String> request;
        private List<String> response;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigInfo {
        private Boolean alarm;
        private int dateeliminateduration;
    }
}
