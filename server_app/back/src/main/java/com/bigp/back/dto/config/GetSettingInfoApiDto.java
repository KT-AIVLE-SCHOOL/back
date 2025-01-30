package com.bigp.back.dto.config;

import java.util.Date;

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
        private Date babyBirth;
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
