package com.bigp.back.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bigp.back.dto.admin.CreateNoticeApiDto;
import com.bigp.back.dto.admin.DeleteNoticeApiDto;
import com.bigp.back.dto.admin.GetNoticeListApiDto;
import com.bigp.back.dto.admin.ReadNoticeApiDto;
import com.bigp.back.dto.admin.UpdateNoticeApiDto;
import com.bigp.back.service.AdminInfoService;
import com.bigp.back.service.NoticeInfoService;
import com.bigp.back.utils.CheckUtils;
import com.bigp.back.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;



@RequestMapping("api/admin")
@RequiredArgsConstructor
@RestController
public class AdminApiController {
    private final NoticeInfoService noticeService;
    private final AdminInfoService adminService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CheckUtils checkUtils;

    @PostMapping("/createNotice")
    public ResponseEntity<?> createNotice(@RequestBody CreateNoticeApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();

        try {
            if (jwtTokenProvider.isExpired(accessToken) || !checkUtils.checkQuery(accessToken)) {
                if (adminService.isAdmin(accessToken))
                    return ResponseEntity.ok(new CreateNoticeApiDto.SuccessResponse(true));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CreateNoticeApiDto.ErrorResponse(false, "권한이 없는 사용자"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CreateNoticeApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CreateNoticeApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @PostMapping("/updateNotice")
    public ResponseEntity<?> updateNotice(@RequestBody UpdateNoticeApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        String header = request.getHeader();
        String body = request.getBody();
        String footer = request.getFooter();

        try {
            if (jwtTokenProvider.isExpired(accessToken) || !checkUtils.checkQuery(accessToken)
                || !checkUtils.checkQuery(header) || !checkUtils.checkQuery(body) || !checkUtils.checkQuery(footer)) {
                if (noticeService.insertNotice(accessToken, header, body, footer))
                    return ResponseEntity.ok(new UpdateNoticeApiDto.SuccessResponse(true));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UpdateNoticeApiDto.ErrorResponse(false, "권한이 없는 사용자"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UpdateNoticeApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UpdateNoticeApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @GetMapping("/getNoticeList")
    public ResponseEntity<?> getNoticeList() {
        try {
            List<Map<String, String>> sendList = noticeService.getNoticeList();

            if (sendList != null)
                return ResponseEntity.ok(new GetNoticeListApiDto.SuccessResponse(true, sendList));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GetNoticeListApiDto.ErrorResponse(false, "기록된 공지사항이 없습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetNoticeListApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @GetMapping("/readNotice")
    public ResponseEntity<?> readNotice(@RequestParam(name="header") String header, @RequestParam(name="writetime") String writetime) {
        try {
            Map<String, String> list = noticeService.readNotice(header, writetime);

            if (list != null)
                return ResponseEntity.ok(new ReadNoticeApiDto.SuccessResponse(true, list.get("header"), list.get("body"), list.get("footer")));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ReadNoticeApiDto.ErrorResponse(false, "내용물이 존재하지 않음"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReadNoticeApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @PostMapping("/deleteNotice")
    public ResponseEntity<?> deleteNotice(@RequestBody DeleteNoticeApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        String header = request.getHeader();
        String writetime = request.getWritetime();
        
        try {
            if (jwtTokenProvider.isExpired(accessToken) || !checkUtils.checkQuery(accessToken)
                || !checkUtils.checkQuery(header) || !checkUtils.checkQuery(writetime)) {
                if (noticeService.deleteNotice(accessToken, header, writetime))
                    return ResponseEntity.ok(new DeleteNoticeApiDto.SuccessResponse(true));
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new DeleteNoticeApiDto.ErrorResponse(false, "존재하지 않는 사용자"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DeleteNoticeApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DeleteNoticeApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
}
