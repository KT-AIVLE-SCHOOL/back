package com.bigp.back.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bigp.back.entity.BabyEmotion;
import com.bigp.back.entity.BabyInfo;
import com.bigp.back.entity.ChatInfo;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.repository.BabyEmotionRepository;
import com.bigp.back.repository.BabyRepository;
import com.bigp.back.repository.ChatRepository;
import com.bigp.back.repository.UserRepository;
import com.bigp.back.utils.DateFormatter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataCleanService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final BabyRepository babyRepository;
    private final BabyEmotionRepository babyEmotionRepository;
    private final DateFormatter transDate;

    @Transactional
    public void dataClean() {
        List<UserInfo> userList = userRepository.findAll();

        for (UserInfo user: userList) {
            try {
                int expired = user.getConfigInfo().getDataeliminateduration();
                int deleteNum;
                String curDate = transDate.formatDateYMD(new Date());
                int curDateInt = Integer.parseInt(curDate.split("-")[0]) * 12 + Integer.parseInt(curDate.split("-")[1]);
                List<ChatInfo> chatList = user.getChatInfoList();
                BabyInfo baby = user.getBabyInfo();
                List<BabyEmotion> babyEmotions = baby.getBabyEmotions();

                for (deleteNum = 0; deleteNum < chatList.size(); deleteNum++) {
                    String dbDate = transDate.formatDateYMD(chatList.get(deleteNum).getRequestTime());
                    int dbDateInt = Integer.parseInt(dbDate.split("-")[0]) * 12 + Integer.parseInt(dbDate.split("-")[1]);

                    if (curDateInt > dbDateInt + expired)
                        break;
                    chatRepository.delete(chatList.get(deleteNum));
                }
                chatList.subList(0, deleteNum).clear();
                user.setChatInfoList(chatList);

                for (deleteNum = 0; deleteNum < babyEmotions.size(); deleteNum++) {
                    String dbDate = babyEmotions.get(deleteNum).getCheckTime().split(" ")[0];
                    int dbDateInt = Integer.parseInt(dbDate.split("-")[0]) * 12 + Integer.parseInt(dbDate.split("-")[1]);

                    if (curDateInt > dbDateInt + expired)
                        break;
                    babyEmotionRepository.delete(babyEmotions.get(deleteNum));
                }
                babyEmotions.subList(0, deleteNum).clear();
                baby.setBabyEmotions(babyEmotions);
                babyRepository.save(baby);
                user.setBabyInfo(baby);
                userRepository.save(user);
            } catch (Exception e) {
                ;
            }
        }
    }
}
