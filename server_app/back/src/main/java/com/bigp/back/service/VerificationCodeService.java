package com.bigp.back.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bigp.back.entity.VerificationCode;
import com.bigp.back.repository.VerificationCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean insertVerificationCode(String email, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (verificationCode == null) {
            verificationCode = new VerificationCode();
            String encodeCode = passwordEncoder.encode(code);
            verificationCode.setEmail(email);
            verificationCode.setCode(encodeCode);
            verificationCodeRepository.save(verificationCode);
            return true;
        }
        return false;
    }

    public int deleteVerificationCode(String email, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (verificationCode != null) {
            String dbCode = verificationCode.getCode();
            verificationCodeRepository.delete(verificationCode);
            if (passwordEncoder.matches(code, dbCode)) {
                return 0;
            }
            return 1;
        }
        return 2;
    }
}
