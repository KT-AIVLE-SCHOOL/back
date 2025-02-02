package com.bigp.back.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bigp.back.service.AdminInfoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final AdminInfoService adminService;

    @Override
    public void run(String... args) throws Exception {
        boolean adminInserted = adminService.insertAdminInfo();
        if (adminInserted)
            System.out.println("Admin information inserted success");
        else
            System.out.println("Admin information already existed");
    }
}
