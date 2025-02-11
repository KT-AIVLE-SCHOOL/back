package com.bigp.back.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bigp.back.service.DataCleanService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DBCleanUp {
    private final DataCleanService cleanService;

    @Scheduled(cron="0 0 0 * * *", zone="Asia/Seoul")
    public void dailyDBCleanTask() {
        cleanService.dataClean();
    }
}
