package com.bigp.back.dto.emotion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PostAnswerApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String accessToken;
        private int emotion;
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
