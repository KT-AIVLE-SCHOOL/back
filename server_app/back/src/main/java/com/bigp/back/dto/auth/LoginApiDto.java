package com.bigp.back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class LoginApiDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String email;
        private String password;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private boolean admin;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessAdminResponse {
        private boolean success;
        private boolean admin;
        private String accessToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
