package com.bigp.back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CheckPassApiDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String password;
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
    public static class ErrorBadRequestResponse {
        private boolean success;
        private boolean checkCharacters;
        private boolean checkPassLen;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
