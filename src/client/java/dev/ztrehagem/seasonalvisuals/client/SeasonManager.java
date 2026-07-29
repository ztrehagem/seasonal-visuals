package dev.ztrehagem.seasonalvisuals.client;

import net.minecraft.client.multiplayer.ClientLevel;

public class SeasonManager {

    public static final long SEASON_LENGTH_TICKS = 24000L * 7; // テスト用に7日間に設定
    private static Season currentSeason = Season.SPRING;

    /**
     * ティック毎に季節の状態を更新します。
     * @return 季節が変更された場合は true
     */
    public static boolean updateSeason(ClientLevel level) {
        if (level == null) return false;
        Season newSeason = Season.fromTick(level.getOverworldClockTime(), SEASON_LENGTH_TICKS);
        if (currentSeason != newSeason) {
            currentSeason = newSeason;
            return true;
        }
        return false;
    }

    /**
     * キャッシュされた現在の季節を取得します（描画処理向けに高速）。
     */
    public static Season getCurrentSeason() {
        return currentSeason;
    }

    /**
     * 季節に応じた葉の色を取得します。
     */
    public static int getFoliageColor(Season season, int originalColor) {
        return switch (season) {
            case AUTUMN -> 0xD35400; // 紅葉カラー
            case WINTER -> 0x808070; // 枯れ木・雪カラー
            default -> originalColor;
        };
    }
}


