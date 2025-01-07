package com.bigp.back.service;

import com.bigp.back.dto.UserDTO;
import com.bigp.back.entity.BabyInfo;
import com.bigp.back.entity.ChatInfo;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void registerUser(UserDTO.UserInfo userInfo) {
        String encodePassword = passwordEncoder.encode(userInfo.getPassword());
        UserInfo user = new UserInfo();
        List<BabyInfo> baby = new ArrayList<>();
        List<ChatInfo> chat = new ArrayList<>();

        user.setAppId(userInfo.getAppId());
        user.setEmail(userInfo.getEmail());
        user.setAccessToken(userInfo.getAccessToken());
        user.setPassword(encodePassword);
        user.setRefreshToken(userInfo.getRefreshToken());
        user.setBabyInfoList(baby);
        user.setChatInfoList(chat);
        userRepository.save(user);
    }

    public void deleteUser(int appId) {
        UserInfo user = userRepository.findByAppId(appId);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    public boolean authenticatorUser(int appId, String rawPassword) {
        UserInfo user = userRepository.findByAppId(appId);
        if (user != null) {
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
