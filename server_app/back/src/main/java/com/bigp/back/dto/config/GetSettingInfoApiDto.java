package com.bigp.back.dto.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class GetSettingInfoApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private boolean alarm;
        private String babyName;
        private String babyBirth;
        private Integer dataEliminateDuration;
        private Integer coreTimeStart;
        private Integer coreTimeEnd;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
