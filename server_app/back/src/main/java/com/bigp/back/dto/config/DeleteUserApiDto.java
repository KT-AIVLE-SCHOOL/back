package com.bigp.back.dto.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class DeleteUserApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String accessToken;
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
