package com.bigp.back.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bigp.back.dto.dashboard.GetBabyEmotionInfoApiDto;
import com.bigp.back.entity.BabyEmotion;
import com.bigp.back.entity.BabyInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BabyEmotionService {
    BabyInfoService babyService;

    public Map<String, String> getBabyInfo(String accessToken) {
        Map<String, String> info = new HashMap<>();

        BabyInfo baby = babyService.getBabyInfo(accessToken);

        if (baby != null) {
            info.put("babyName", baby.getBabyname());
            info.put("babyBirth", baby.getBabybirth());
            info.put("code", "200");
        } else {
            info.put("code", "404");
        }
        return info;
    }
    
    public GetBabyEmotionInfoApiDto.RawData getBabyEmotionInfo(String accessToken) {
        BabyInfo baby = babyService.getBabyInfo(accessToken);

        if (baby != null) {
            List<BabyEmotion> emotion = baby.getBabyEmotions();
            // List<BabyEmotion> emotion = baby.getBabyEmotions().reversed();
            if (emotion != null) {
                List<GetBabyEmotionInfoApiDto.BabyRecently> emotionsMap = emotion.stream()
                    .map(e -> new GetBabyEmotionInfoApiDto.BabyRecently(e.getCheckTime(), e.getEmotion()))
                    .collect(Collectors.toList());
                    // .reversed();
                
                List<GetBabyEmotionInfoApiDto.BabyRecently> recentlys = emotionsMap.stream()
                    .limit(15)
                    .collect(Collectors.toList());
                
                Map<Integer, Map<Integer, Long>> groupedEmotions = emotionsMap.stream()
                    .collect(Collectors.groupingBy(
                        dto -> Integer.parseInt(dto.getBabyEmotionTime().split(" ")[1].split(":")[0]),
                        Collectors.groupingBy(GetBabyEmotionInfoApiDto.BabyRecently::getBabyEmotionNum,
                        Collectors.counting())
                    ));
                List<GetBabyEmotionInfoApiDto.BabyEmotionOrderByTime> sortingEmotions = groupedEmotions.entrySet().stream()
                    .map(entry -> {
                        int hour = entry.getKey();
                        Map<Integer, Long> emotions = entry.getValue();
                        long total = emotions.values().stream().mapToLong(Long::longValue).sum();
                        Map.Entry<Integer, Long> maxEmotion = emotions.entrySet().stream()
                            .max(Map.Entry.comparingByValue()).orElse(null);
                        
                        return new GetBabyEmotionInfoApiDto.BabyEmotionOrderByTime(hour,
                            maxEmotion != null ? maxEmotion.getKey() : 0,
                            maxEmotion != null ? (float) maxEmotion.getValue() / total : 0
                        );
                    })
                    .sorted(Comparator.comparingInt(GetBabyEmotionInfoApiDto.BabyEmotionOrderByTime::getHour))
                    .collect(Collectors.toList());

                return new GetBabyEmotionInfoApiDto.RawData("200", recentlys, sortingEmotions);
            }
        }
        return new GetBabyEmotionInfoApiDto.RawData("404", null, null);
    }
}
