package com.bigp.back.service;

import java.nio.charset.StandardCharsets;

import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AESService {
    private final AesBytesEncryptor encryptor;

    public String encryptInfo(String info) {
        byte[] encrypt = encryptor.encrypt(info.getBytes(StandardCharsets.UTF_8));
        
        return byteArrayToString(encrypt);
    }

    public String decryptInfo(String info) {
        byte[] decryptBytes = stringToByteArray(info);
        byte[] decrypt = encryptor.decrypt(decryptBytes);
        
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    private String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }

    private byte[] stringToByteArray(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }

        return bytes;
    }
}
