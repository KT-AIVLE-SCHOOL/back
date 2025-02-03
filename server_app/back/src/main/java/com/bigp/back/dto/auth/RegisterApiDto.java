package com.bigp.back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class RegisterApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String username;
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
