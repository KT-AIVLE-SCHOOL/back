package com.bigp.back.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.bigp.back.dto.UserDTO;
import com.bigp.back.entity.BabyInfo;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.repository.BabyRepository;
import com.bigp.back.repository.UserRepository;
import com.bigp.back.utils.DateFormatter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BabyInfoService {
    private final UserInfoService userInfoService;
    private final UserRepository userRepository;
    private final BabyRepository babyRepository;
    private final DateFormatter transDate;

    public boolean insertBabyInfo(String accessToken) {
        UserInfo userInfo = userInfoService.getUserInfo("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo babyInfo = userInfo.getBabyInfo();

            if (babyInfo != null)
                babyRepository.delete(babyInfo);

            babyInfo = new BabyInfo();

            babyInfo.setBabyname("");
            babyInfo.setBabybirth("");
            babyRepository.save(babyInfo);
            userInfo.setBabyInfo(babyInfo);
            userRepository.save(userInfo);
            return true;
        }
        return false;
    }

    public boolean updateBabyInfo(String accessToken, UserDTO.BabyInfo babyDto) {
        UserInfo userInfo = userInfoService.getUserInfo("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo babyInfo = userInfo.getBabyInfo();

            if (babyInfo != null) {
                babyInfo.setBabyname(babyDto.getBabyname());
                babyInfo.setBabybirth(babyDto.getBabybirth());
                babyRepository.save(babyInfo);
                return true;
            }
        }
        return false;
    }

    public BabyInfo getBabyInfo(String accessToken) {
        UserInfo userInfo = userInfoService.getUserInfo("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo babyInfo = userInfo.getBabyInfo();
            return babyInfo;
        }
        return null;
    }

    public boolean deleteBabyInfo(String accessToken) {
        UserInfo userInfo = userInfoService.getUserInfo("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo babyInfo = userInfo.getBabyInfo();
            if (babyInfo != null) {
                babyRepository.delete(babyInfo);
                return true;
            }
        }
        return false;
    }
}
