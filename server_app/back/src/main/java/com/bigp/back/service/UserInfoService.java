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
    private final AESService aesService;

    public boolean insertUser(RegisterApiDto.RequestBody register) {
        UserInfo user = userRepository.findByEmail(register.getEmail());
        if (user == null) {
            user = new UserInfo();
            String encodePassword = passwordEncoder.encode(register.getPassword());
            String accessToken = jwtTokenProvider.createToken(register.getEmail(), true);
            String refreshToken = jwtTokenProvider.createToken(register.getEmail(), false);
            user.setEmail(aesService.encryptInfo(register.getEmail()));
            user.setPassword(encodePassword);
            user.setUsername(aesService.encryptInfo(register.getUsername()));
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
        UserInfo user = userRepository.findByEmail(aesService.encryptInfo(email));

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
                user.setEmail(aesService.encryptInfo(update.getEmail()));
            }
            if (update.getUsername() != null) {
                user.setUsername(aesService.encryptInfo(update.getUsername()));
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

    public UserDTO.UserInfo getUserInfo(String key, String value) {
        UserInfo userInfo;
        UserDTO.UserInfo user = new UserDTO.UserInfo();

        if (key.equals("accessToken"))
            userInfo = userRepository.findByAccessToken(value);
        else
            userInfo = userRepository.findByEmail(aesService.encryptInfo(value));
        user.setAccessToken(userInfo.getAccessToken());
        user.setAliasname(userInfo.getAliasname());
        user.setEmail(aesService.decryptInfo(userInfo.getEmail()));
        user.setPassword(userInfo.getPassword());
        user.setRefreshToken(userInfo.getRefreshToken());
        user.setUsername(aesService.decryptInfo(userInfo.getUsername()));
        user.setProfileImage(Base64.getEncoder().encodeToString(userInfo.getProfileImage()));
        return user;
    }

    public UserInfo getEntity(String key, String value) {
        UserInfo user;

        if (key.equals("accessToken"))
            user = userRepository.findByAccessToken(value);
        else
            user = userRepository.findByEmail(value);
        return user;
    }

    public boolean isUser(String key, String value) {
        UserInfo user;

        if (key.equals("accessToken"))
            user = userRepository.findByAccessToken(value);
        else
            user = userRepository.findByEmail(value);
        
        if (user == null)
            return false;
        return true;
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
