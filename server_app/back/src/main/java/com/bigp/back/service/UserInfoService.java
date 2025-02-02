package com.bigp.back.service;

import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bigp.back.dto.UserDTO;
import com.bigp.back.dto.auth.RegisterApiDto;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.repository.UserRepository;
import com.bigp.back.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean insertUser(RegisterApiDto.RequestBody register) {
        UserInfo user = userRepository.findByEmail(register.getEmail());
        if (user == null) {
            user = new UserInfo();
            String encodePassword = passwordEncoder.encode(register.getPassword());
            String accessToken = jwtTokenProvider.createToken(register.getEmail(), true);
            String refreshToken = jwtTokenProvider.createToken(register.getEmail(), false);
            user.setEmail(register.getEmail());
            user.setPassword(encodePassword);
            user.setUsername(register.getUsername());
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String accessToken) {
        UserInfo user = userRepository.findByAccessToken(accessToken);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public boolean isMatchedUser(String email, String password) {
        UserInfo user = userRepository.findByEmail(email);

        if (user != null)
            return passwordEncoder.matches(password, user.getPassword());
        return false;
    }

    public boolean updateUser(String key, String value, UserDTO.UserInfo update) {
        UserInfo user;
        if (key.equals("accessToken"))
            user = userRepository.findByAccessToken(value);
        else
            user = userRepository.findByEmail(value);

        if (user != null) {
            if (update.getEmail() != null) {
                user.setEmail(update.getEmail());
            }
            if (update.getUsername() != null) {
                user.setUsername(update.getUsername());
            }
            if (update.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(update.getPassword()));
            }
            if (update.getAliasname() != null) {
                user.setAliasname(update.getAliasname());
            }
            if (update.getProfileImage() != null) {
                try {
                    user.setProfileImage(update.getProfileImage().getBytes());  
                } catch (Exception e) {
                    ;
                }
            }
            if (update.getAccessToken() != null) {
                user.setAccessToken(update.getAccessToken());
            }
            if (update.getRefreshToken() != null) {
                user.setRefreshToken(update.getRefreshToken());
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public UserInfo getUserInfo(String key, String value) {
        UserInfo user;

        if (key.equals("accessToken"))
            user = userRepository.findByAccessToken(value);
        else
            user = userRepository.findByEmail(value);
        return user;
    }

    public int setProfileImage(String key, String value, String profileImage) {
        UserInfo user;

        if (key.equals("accessToken")) {
            user = userRepository.findByAccessToken(value);
        } else {
            user = userRepository.findByEmail(value);
        }

        try {
            if (user != null) {
                user.setProfileImage(Base64.getDecoder().decode(profileImage));
                userRepository.save(user);
                return 0;
            }
        } catch (Exception e) {
            return 1;
        }
        return 2;
    }

    public String getProfileImage(String key, String value) {
        UserInfo user;

        if (key.equals("accessToken")) {
            user = userRepository.findByAccessToken(value);
        } else {
            user = userRepository.findByEmail(value);
        }

        if (user != null) {
            String profile = Base64.getEncoder().encodeToString(user.getProfileImage());
            return profile;
        }
        return null;
    }
}
