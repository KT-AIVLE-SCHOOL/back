package com.bigp.back.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bigp.back.dto.emotion.UploadAudioApiDto;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("api/emotion")
@RequiredArgsConstructor
public class EmotionApiController {
    @Value("${emoteai.host}")
    private String aiHost;

    @GetMapping("/uploadAudio")
    public ResponseEntity<?> uploadAudio(@RequestParam(name="filename") String filename, @RequestParam(name="audio") String rawAudio) {
        byte[] audio = Base64.getDecoder().decode(rawAudio);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audio, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            aiHost,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        return ResponseEntity.ok(new UploadAudioApiDto.SuccessResponse(true, 1));
    }
}
