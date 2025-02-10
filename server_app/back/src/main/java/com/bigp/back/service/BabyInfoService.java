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
    private final AESService aesService;

    public boolean insertBabyInfo(String accessToken) {
        UserInfo userInfo = userInfoService.getEntity("accessToken", accessToken);

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
        UserInfo userInfo = userInfoService.getEntity("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo babyInfo = userInfo.getBabyInfo();

            if (babyInfo != null) {
                babyInfo.setBabyname(aesService.encryptInfo(babyDto.getBabyname()));
                babyInfo.setBabybirth(aesService.encryptInfo(babyDto.getBabybirth()));
                babyRepository.save(babyInfo);
                return true;
            }
        }
        return false;
    }

    public UserDTO.BabyInfo getBabyInfo(String accessToken) {
        UserInfo userInfo = userInfoService.getEntity("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo babyInfo = userInfo.getBabyInfo();
            UserDTO.BabyInfo baby = new UserDTO.BabyInfo(aesService.decryptInfo(babyInfo.getBabyname()), aesService.decryptInfo(babyInfo.getBabybirth()));
            return baby;
        }
        return null;
    }

    public BabyInfo getEntity(String accessToken) {
        UserInfo userInfo = userInfoService.getEntity("accessToken", accessToken);

        if (userInfo != null) {
            BabyInfo baby = userInfo.getBabyInfo();
            return baby;
        }
        return null;
    }

    public boolean deleteBabyInfo(String accessToken) {
        UserInfo userInfo = userInfoService.getEntity("accessToken", accessToken);

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
