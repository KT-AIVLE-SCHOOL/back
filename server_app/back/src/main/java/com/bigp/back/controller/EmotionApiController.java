package com.bigp.back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

import com.bigp.back.dto.emotion.PostAnswerApiDto;
import com.bigp.back.service.BabyEmotionService;
import com.bigp.back.utils.JwtTokenProvider;


@RestController
@RequestMapping("api/emotion")
@RequiredArgsConstructor
public class EmotionApiController {
    private final JwtTokenProvider jwtTokenProvider;
    private final BabyEmotionService emotionService;

    @PostMapping("/postEmotion")
    public ResponseEntity<?> postEmotion(@RequestBody PostAnswerApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        int emotion = request.getEmotion();

        try {
            if (jwtTokenProvider.isExpired(accessToken)) {
                if (emotionService.insertBabyEmotionInfo(accessToken, emotion))
                    return ResponseEntity.ok(new PostAnswerApiDto.SuccessResponse(true));
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PostAnswerApiDto.ErrorResponse(false, "아이 정보가 없습니다"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PostAnswerApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PostAnswerApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
}
