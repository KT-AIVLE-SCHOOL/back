package com.bigp.back.dto.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class GetProfileImageApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        boolean success;
        String profileImage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        boolean success;
        String message;
    }
}
