package com.bigp.back.dto.emotion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UploadAudioApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestParameter {
        private String filename;
        private String audio;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private int emotion;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
