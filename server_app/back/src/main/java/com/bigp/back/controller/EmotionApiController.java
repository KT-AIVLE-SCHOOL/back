package com.bigp.back.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bigp.back.dto.emotion.UploadAudioApiDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/emotion")
@RequiredArgsConstructor
public class EmotionApiController {
    @Value("${ai.protocols}")
    private String aiProtocols;
    @Value("${emoteai.host}")
    private String aiHost;

    @PostMapping("/uploadAudio")
    public ResponseEntity<?> uploadAudio(@RequestParam(name="filename") String filename, @RequestParam(name="audio") String rawAudio) {
        byte[] audio = Base64.getDecoder().decode(rawAudio);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource resource = new ByteArrayResource(audio) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            aiProtocols + "://" + aiHost + "/api/uploadAudio",
            HttpMethod.POST,
            requestEntity,
            String.class
        );
        // JSON 파싱을 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // response.getBody()로 JSON 문자열을 가져와 JsonNode로 파싱
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // "message" 필드의 값을 추출
            String message = jsonNode.get("message").asText();
            
            // SuccessResponse 객체 생성 및 반환
                return ResponseEntity.ok(new UploadAudioApiDto.SuccessResponse(true, Integer.valueOf(message)));
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UploadAudioApiDto.ErrorResponse(false, "서버 내부 오류"));
        }
    }
}
