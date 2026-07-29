package dev.ztrehagem.seasonalvisuals.client;

public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER;

    /**
     * ワールドの経過時間（Tick）と季節の長さから対応する季節を算出します。
     */
    public static Season fromTick(long overworldClockTime, long seasonLengthTicks) {
        long cycle = Math.floorMod(overworldClockTime / seasonLengthTicks, 4);
        return switch ((int) cycle) {
            case 0 -> SPRING;
            case 1 -> SUMMER;
            case 2 -> AUTUMN;
            case 3 -> WINTER;
            default -> SPRING;
        };
    }

    /**
     * 次の季節を取得します。
     */
    public Season next() {
        return switch (this) {
            case SPRING -> SUMMER;
            case SUMMER -> AUTUMN;
            case AUTUMN -> WINTER;
            case WINTER -> SPRING;
        };
    }
}
