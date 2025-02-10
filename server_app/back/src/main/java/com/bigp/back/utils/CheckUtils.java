package com.bigp.back.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import jakarta.persistence.Query;

@Component
public class CheckUtils {
    public boolean checkEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

        return Pattern.compile(emailRegex)
                    .matcher(email)
                    .matches();
    }

    public boolean checkQuery(String input) {
        String sqlPattern = "^(SELECT|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER|TRUNCATE)\\s+.*$";
        Pattern pattern = Pattern.compile(sqlPattern, Pattern.CASE_INSENSITIVE);
        
        return pattern.matcher(input.trim()).matches();
    }

    public int checkPassword(String password) {
        int combination = (password.matches(".*\\d.*") ? 1 : 0) +
                          (password.matches(".*[a-zA-Z].*") ? 1 : 0) +
                          (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*") ? 1 : 0);
        int len = password.length();

        if (combination > 1 && len > 7 && len < 21)
            return 0;
        if (combination > 1)
            return 1;
        if (len > 7 && len < 21)
            return 2;
        return 3;
    }

    public String createPass() {
        String numbers = "0123456789";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String special = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder(9);

        for (int i = 0; i < 4; i++) {
            result.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        for (int i = 0; i < 4; i++) {
            result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        result.append(special.charAt(random.nextInt(special.length())));

        return result.toString();
    }

    public String checkVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
