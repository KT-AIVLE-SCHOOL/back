package com.bigp.back.dto.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class GetBabyEmotionInfoApiDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RawData {
        private String code;
        private List<BabyRecently> babyRecently;
        private List<BabyEmotionOrderByTime> babyEmotionOrderByTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SuccessResponse {
        private boolean success;
        private List<BabyRecently> babyRecently;
        private List<BabyEmotionOrderByTime> babyEmotionOrderByTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private boolean success;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class BabyRecently {
        private String babyEmotionTime;
        private int babyEmotionNum;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class BabyEmotionOrderByTime {
        private int hour;
        private int maxEmotion;
        private float ratio;
    }
}
