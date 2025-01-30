package com.bigp.back.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRE_TIME_ACCESS = 1000 * 60 * 60 * 24; // 1일
    private static final long EXPIRE_TIME_REFRESH = 7 * 1000 * 60 * 60 * 24; // 7일

    public String createToken(String email, boolean isAccess) {
        Date now = new Date();
        Date expiryDate;
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        if (isAccess)
            expiryDate = new Date(now.getTime() + EXPIRE_TIME_ACCESS);
        else
            expiryDate = new Date(now.getTime() + EXPIRE_TIME_REFRESH);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean isExpired(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

            Date expirationDate = claimsJws.getPayload().getExpiration();
            Date now = new Date();

            return !expirationDate.before(now);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            return false;
        }
    }
}
