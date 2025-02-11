package com.bigp.back.dto.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SetSettingInfoApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String accessToken;
        private boolean alarm;
        private String babyName;
        private String babyBirth;
        private Integer dataEliminateDuration;
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
