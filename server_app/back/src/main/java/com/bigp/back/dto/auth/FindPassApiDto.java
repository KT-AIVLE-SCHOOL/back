package com.bigp.back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class FindPassApiDto {
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
