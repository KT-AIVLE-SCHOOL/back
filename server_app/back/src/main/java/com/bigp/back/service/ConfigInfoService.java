package com.bigp.back.service;

import org.springframework.stereotype.Service;

import com.bigp.back.dto.UserDTO;
import com.bigp.back.entity.ConfigInfo;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.repository.ConfigRepository;
import com.bigp.back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfigInfoService {
    private final UserRepository userRepository;
    private final ConfigRepository configRepository;

    public boolean insertConfigInfo(String accessToken) {
        UserInfo userInfo = userRepository.findByAccessToken(accessToken);

        if (userInfo != null) {
            ConfigInfo configInfo = userInfo.getConfigInfo();
            if (configInfo != null)
                configRepository.delete(configInfo);
            configInfo = new ConfigInfo();

            configInfo.setAlarm(false);
            configInfo.setCoretimeend(10);
            configInfo.setCoretimestart(7);
            configInfo.setDataeliminateduration(14);
            configRepository.save(configInfo);
            return true;
        }
        return false;
    }

    public boolean updateConfigInfo(String accessToken, UserDTO.ConfigInfo info) {
        UserInfo user = userRepository.findByAccessToken(accessToken);

        if (user != null) {
            ConfigInfo config = user.getConfigInfo();
            if (config != null) {
                config.setAlarm(info.getAlarm());
                config.setCoretimestart(info.getCoretimestart());
                config.setCoretimeend(info.getCoretimeend());
                config.setDataeliminateduration(info.getDateeliminateduration());
                configRepository.save(config);
                return true;
            }
        }
        return false;
    }

    public ConfigInfo getConfigInfo(String accessToken) {
        UserInfo user = userRepository.findByAccessToken(accessToken);

        if (user != null) {
            ConfigInfo config = user.getConfigInfo();
            if (config != null) {
                return config;
            }
        }
        return null;
    }

    public boolean deleteConfigInfo(String accessToken) {
        UserInfo user = userRepository.findByAccessToken(accessToken);

        if (user != null) {
            ConfigInfo config = user.getConfigInfo();
            if (config != null) {
                configRepository.delete(config);
                return true;
            }
        }
        return false;
    }
}
