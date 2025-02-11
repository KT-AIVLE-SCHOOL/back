package com.bigp.back.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigp.back.dto.dashboard.GetBabyEmotionInfoApiDto;
import com.bigp.back.dto.dashboard.GetBabyInfoApiDto;
import com.bigp.back.service.BabyEmotionService;
import com.bigp.back.utils.CheckUtils;
import com.bigp.back.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;



@RequestMapping("/api/dashboard")
@RestController
@RequiredArgsConstructor
public class DashboardApiController {
    private final BabyEmotionService babyEmotionService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CheckUtils checkUtils;

    @GetMapping("/getBabyInfo")
    public ResponseEntity<?> getBabyInfo(@CookieValue(name="accessToken", required=true) String accessToken) {
        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                Map<String, String> info = babyEmotionService.getBabyInfo(accessToken);

                if (info.get("code").equals("200"))
                    return ResponseEntity.ok(new GetBabyInfoApiDto.SuccessResponse(true, info.get("babyName"), info.get("babyBirth")));
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new GetBabyInfoApiDto.ErrorResponse(false, "해당 데이터가 없습니다"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GetBabyInfoApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetBabyInfoApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @GetMapping("/getBabyEmotionInfo")
    public ResponseEntity<?> getBabyEmotionInfo(@CookieValue(name="accessToken", required=true) String accessToken) {
        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                GetBabyEmotionInfoApiDto.RawData data = babyEmotionService.getBabyEmotionInfo(accessToken);

                if (data.getCode().equals("200") && data.getBabyRecently().isEmpty()) {
                    return ResponseEntity.ok(new GetBabyEmotionInfoApiDto.SuccessResponse(false, data.getBabyRecently(), data.getBabyEmotionOrderByTime()));
                } else if (data.getCode().equals("200")) {
                    return ResponseEntity.ok(new GetBabyEmotionInfoApiDto.SuccessResponse(true, data.getBabyRecently(), data.getBabyEmotionOrderByTime()));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new GetBabyEmotionInfoApiDto.ErrorResponse(false, "데이터가 없습니다"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GetBabyEmotionInfoApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetBabyEmotionInfoApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        }
    }
    
}
