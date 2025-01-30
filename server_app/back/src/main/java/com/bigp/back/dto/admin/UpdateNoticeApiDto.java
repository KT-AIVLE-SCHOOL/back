package com.bigp.back.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UpdateNoticeApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String accessToken;
        private String header;
        private String body;
        private String footer;
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
