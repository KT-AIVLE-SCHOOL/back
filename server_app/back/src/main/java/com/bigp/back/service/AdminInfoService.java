package com.bigp.back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bigp.back.dto.AdminDTO;
import com.bigp.back.entity.AdminInfo;
import com.bigp.back.repository.AdminRepository;
import com.bigp.back.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminInfoService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${admin.id}")
    String adminId;

    @Value("${admin.password}")
    String adminPass;

    public boolean insertAdminInfo() {
        AdminInfo admin = adminRepository.findByAdminId(adminId);

        if (admin == null) {
            admin = new AdminInfo();
            admin.setAdminId(adminId);
            admin.setPassword(passwordEncoder.encode(adminPass));
            admin.setAccessToken(jwtTokenProvider.createToken(adminId, false));
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    public boolean updateAdminInfo(String key, String value, AdminDTO.AdminInfo info) {
        AdminInfo admin;
        if (key.equals("accessToken"))
            admin = adminRepository.findByAccessToken(value);
        else
            admin = adminRepository.findByAdminId(value);
        
        if (admin != null) {
            if (info.getAccessToken() != null)
                admin.setAccessToken(info.getAccessToken());
            if (info.getAdminId() != null)
                admin.setAdminId(info.getAdminId());
            if (info.getPassword() != null)
                admin.setPassword(passwordEncoder.encode(info.getPassword()));
            adminRepository.save(admin);
            return true;
        }

        return true;
    }

    public boolean isAdmin(String accessToken) {
        AdminInfo admin = adminRepository.findByAccessToken(accessToken);

        if (admin != null)
            return true;
        return false;
    }

    public boolean isMatchedAdmin(String id, String password) {
        AdminInfo admin = adminRepository.findByAdminId(id);

        if (admin != null)
            return passwordEncoder.matches(password, admin.getPassword());
        return false;
    }

    public boolean deleteAdmin(String accessToken) {
        adminRepository.deleteAll();
        return true;
    }

    public AdminInfo getAdminInfo(String accessToken) {
        return adminRepository.findByAccessToken(accessToken);
    }
}
