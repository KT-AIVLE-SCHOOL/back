package com.bigp.back.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ReadNoticeApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestParameter {
        private String header;
        private String writetime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private String header;
        private String body;
        private String footer;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
