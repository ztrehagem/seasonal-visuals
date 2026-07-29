package dev.ztrehagem.seasonalvisuals.client;

import net.minecraft.client.Minecraft;

public class SeasonManager {

    public enum Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    // 1季節あたりの日数（例: 28日 = 672,000 ティック）
    private static final long SEASON_LENGTH_TICKS = 24000L * 7; // テスト用に7日間に設定

    public static Season getCurrentSeason() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return Season.SPRING;

        // ワールドの総経過時間から現在の季節を算出
        long time = client.level.getOverworldClockTime();
        long cycle = (time / SEASON_LENGTH_TICKS) % 4;

        switch ((int) cycle) {
            case 0: return Season.SPRING;
            case 1: return Season.SUMMER;
            case 2: return Season.AUTUMN;
            case 3: return Season.WINTER;
            default: return Season.SPRING;
        }
    }
}
