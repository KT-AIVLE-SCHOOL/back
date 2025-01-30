package com.bigp.back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CheckVerificationCodeApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String email;
        private String code;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
