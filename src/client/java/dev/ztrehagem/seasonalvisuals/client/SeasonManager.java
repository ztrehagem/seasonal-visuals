package dev.ztrehagem.seasonalvisuals.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

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
     * バイオームの特性（常緑、サバンナの乾季/雨季など）に応じた色補正を行います。
     */
    public static int getFoliageColor(Biome biome, Season season, int originalColor) {
        // 常緑バイオーム（ジャングル、マングローブ）は季節変化なし
        if (isEvergreenBiome(biome)) {
            return originalColor;
        }

        // サバンナバイオーム（雨季・乾季表現）
        if (isSavannaBiome(biome)) {
            return switch (season) {
                case SPRING -> originalColor; // 雨季の始まり（バニラ本来の色）
                case SUMMER -> applySummerTint(originalColor); // 雨季ピーク（青々とした緑）
                case AUTUMN, WINTER -> applyDrySeasonTint(originalColor); // 乾季（乾いた黄金〜オリーブ褐色）
            };
        }

        // 通常の季節変化バイオーム
        return switch (season) {
            case SPRING -> originalColor; // 春：デフォルト（Vanilla標準の爽やかな新緑）
            case SUMMER -> applySummerTint(originalColor); // 夏：深緑・青々とした濃い緑
            case AUTUMN -> applyAutumnTint(originalColor); // 秋：暖かみのある紅葉・黄金色
            case WINTER -> applyWinterTint(originalColor); // 冬：彩度を落とした枯れ木・寒色
        };
    }

    /**
     * 常緑バイオーム（ジャングル・マングローブ等）かどうかを判定します。
     */
    public static boolean isEvergreenBiome(Biome biome) {
        if (biome == null) return false;
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return false;

        var registryOpt = client.level.registryAccess().lookup(Registries.BIOME);
        if (registryOpt.isEmpty()) return false;

        var registry = registryOpt.get();
        var keyOpt = registry.getResourceKey(biome);
        if (keyOpt.isPresent()) {
            ResourceKey<Biome> key = keyOpt.get();
            return key.equals(Biomes.JUNGLE)
                || key.equals(Biomes.SPARSE_JUNGLE)
                || key.equals(Biomes.BAMBOO_JUNGLE)
                || key.equals(Biomes.MANGROVE_SWAMP);
        }

        return false;
    }

    /**
     * サバンナ系バイオームかどうかを判定します。
     */
    public static boolean isSavannaBiome(Biome biome) {
        if (biome == null) return false;
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return false;

        var registryOpt = client.level.registryAccess().lookup(Registries.BIOME);
        if (registryOpt.isEmpty()) return false;

        var registry = registryOpt.get();
        var keyOpt = registry.getResourceKey(biome);
        if (keyOpt.isPresent()) {
            ResourceKey<Biome> key = keyOpt.get();
            return key.equals(Biomes.SAVANNA)
                || key.equals(Biomes.SAVANNA_PLATEAU)
                || key.equals(Biomes.WINDSWEPT_SAVANNA);
        }

        return false;
    }

    /**
     * サバンナの乾季（秋・冬）用カラー補正：
     * 青味を抑え、赤・黄色味を強めて乾燥した黄金色〜オリーブ褐色にします。
     */
    private static int applyDrySeasonTint(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int newR = (int) (r * 1.18);
        int newG = (int) (g * 0.92);
        int newB = (int) (b * 0.45);

        return (clamp(newR) << 16) | (clamp(newG) << 8) | clamp(newB);
    }

    /**
     * 夏のカラー補正：赤・青成分を少し抑え、深みと青みのある豊かな緑色にシフトします。
     */
    private static int applySummerTint(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int newR = (int) (r * 0.70);
        int newG = (int) (g * 0.92);
        int newB = (int) (b * 0.75);

        return (clamp(newR) << 16) | (clamp(newG) << 8) | clamp(newB);
    }

    /**
     * 秋のカラー補正：バイオームの明るさを維持しながら、暖かみのあるオレンジ・黄金色にシフトします。
     */
    private static int applyAutumnTint(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        double luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0;
        double factor = Math.min(1.0, luminance * 1.6);

        int newR = (int) (225 * factor);
        int newG = (int) (105 * factor);
        int newB = (int) (20 * factor);

        return (clamp(newR) << 16) | (clamp(newG) << 8) | clamp(newB);
    }

    /**
     * 冬のカラー補正：彩度を下げ、冷たい枯れ木・雪景色に馴染む色合いにします。
     */
    private static int applyWinterTint(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);

        int newR = (int) (r * 0.35 + gray * 0.55);
        int newG = (int) (g * 0.35 + gray * 0.55);
        int newB = (int) (b * 0.35 + gray * 0.60);

        return (clamp(newR) << 16) | (clamp(newG) << 8) | clamp(newB);
    }

    private static int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }
}


