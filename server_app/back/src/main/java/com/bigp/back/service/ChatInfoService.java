package com.bigp.back.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bigp.back.dto.UserDTO;
import com.bigp.back.entity.ChatInfo;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.repository.ChatRepository;
import com.bigp.back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatInfoService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public boolean insertChatInfo(String accessToken, UserDTO.ChatInfo chat) {
        UserInfo user = userRepository.findByAccessToken(accessToken);

        if (user != null) {
            List<ChatInfo> chatInfoList = user.getChatInfoList();

            if (chatInfoList == null)
                chatInfoList = new ArrayList<>();
            
            ChatInfo chatInfo = new ChatInfo();

            chatInfo.setChat(user);
            chatInfo.setRequest(chat.getRequest());
            chatInfo.setResponse(chat.getResponse());
            chatInfo.setRequestTime(chat.getRequestTime());
            chatRepository.save(chatInfo);

            chatInfoList.add(chatInfo);
            user.setChatInfoList(chatInfoList);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Map<String, Object> recentlyChatInfo(String accessToken) {
        UserInfo user = userRepository.findByAccessToken(accessToken);

        if (user != null) {
            ChatInfo chat = user.getChatInfoList().getLast();

            if (chat != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("request", chat.getRequest());
                data.put("response", chat.getResponse());
                data.put("time", chat.getRequestTime());
                return data;
            }
        }
        return null;
    }

    public boolean deleteChatInfo(String accessToken, Date time) {
        UserInfo user = userRepository.findByAccessToken(accessToken);

        return false;
    }
}
