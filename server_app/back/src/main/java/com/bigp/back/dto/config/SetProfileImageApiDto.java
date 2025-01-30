package com.bigp.back.dto.config;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SetProfileImageApiDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestBody {
        private String accessToken;
        private MultipartFile profileImage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private String image;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }
}
