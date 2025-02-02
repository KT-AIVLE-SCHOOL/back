package com.bigp.back.cofiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.bigp.back.dto.UserDTO;
import com.bigp.back.service.ChatInfoService;
import com.bigp.back.service.UserInfoService;
import com.bigp.back.utils.DateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.chatWebSocketHandler, "/chat")
        .setAllowedOrigins("*");
    }
}

@Component
@RequiredArgsConstructor
class ChatWebSocketHandler extends TextWebSocketHandler {
    @Value("${ai.protocols}")
    private String aiProtocol;

    @Value("${chatai.host}")
    private String chataiHost;

    @Value("${chatai.port}")
    private String chataiPort;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final UserInfoService userService;
    private final ChatInfoService chatService;
    private final DateFormatter transDate;
    
    private Map<WebSocketSession, SessionData> sessionDataMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connecting Websocket");
        sessionDataMap.put(session, new SessionData());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectNode questionMes = objectMapper.readValue(message.getPayload(), ObjectNode.class);
        SessionData sessionData = sessionDataMap.get(session);

        if ("Greeting".equals(questionMes.get("header").asText()))
            handleGreeting(session, questionMes, sessionData);
        else if ("Question".equals(questionMes.get("header").asText()))
            handleQuestion(session, questionMes, sessionData);
        else if ("Goodbye".equals(questionMes.get("header").asText()))
            session.close();
    }

    private void handleGreeting(WebSocketSession session, ObjectNode questionMes, SessionData sessionData) throws IOException {
        sessionData.accessToken = questionMes.get("accessToken").asText();
        boolean success = userService.isUser("accessToken", sessionData.accessToken);

        if (!success) {
            session.close();
            return ;
        }

        Map<String, Object> info = chatService.recentlyChatInfo(sessionData.accessToken);
        ObjectNode data = objectMapper.createObjectNode();
        if (info != null) {
            data.set("request", objectMapper.valueToTree(info.get("request")));
            data.set("response", objectMapper.valueToTree(info.get("response")));
        } else {
            data.putArray("request");
            data.putArray("response");
        }
        this.sendMessage(session, "Greeting", data);
    }

    private void handleQuestion(WebSocketSession session, ObjectNode questionsMes, SessionData sessionData) throws IOException {
        String question = questionsMes.get("data").asText();
        String url = String.format("%s://%s:%s/api/getAnswer?question=%s", aiProtocol, chataiHost, chataiPort, question);
        String answer = restTemplate.getForObject(url, Map.class).get("answer").toString();

        sessionData.que.add(question);
        sessionData.res.add(answer);

        this.sendMessage(session, "Answer", objectMapper.valueToTree(answer));
    }

    private void sendMessage(WebSocketSession session, String header, Object data) throws IOException {
        ObjectNode sendMes = objectMapper.createObjectNode();
        sendMes.put("type", "message");
        sendMes.put("header", header);
        sendMes.set("data", objectMapper.valueToTree(data));
        sendMes.put("time", transDate.formatDateYMDHMS(new Date()));
        session.sendMessage(new TextMessage(sendMes.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionData sessionData = sessionDataMap.get(session);
        if (!sessionData.que.isEmpty() && !sessionData.res.isEmpty()) {
            UserDTO.ChatInfo chat = new UserDTO.ChatInfo();
            chat.setRequestTime(new Date());
            chat.setRequest(sessionData.que);
            chat.setResponse(sessionData.res);
            chatService.insertChatInfo(sessionData.accessToken, chat);
        }
    }

    private static class SessionData {
        String accessToken;
        Date time;
        List<String> que = new ArrayList<>();
        List<String> res = new ArrayList<>();
    }
}
