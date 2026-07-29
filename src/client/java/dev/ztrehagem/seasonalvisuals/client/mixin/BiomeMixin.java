package dev.ztrehagem.seasonalvisuals.client.mixin;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ztrehagem.seasonalvisuals.client.SeasonManager;

@Mixin(Biome.class)
public class BiomeMixin {
	// @Inject(at = @At("HEAD"), method = "run")
	// private void init(CallbackInfo info) {
	// // This code is injected into the start of Minecraft.run()V
	// }

	// 葉っぱ（Foliage）の色計算に介入
	@Inject(method = "getFoliageColor", at = @At("RETURN"), cancellable = true)
	private void modifyFoliageColor(CallbackInfoReturnable<Integer> cir) {
		// int originalColor = cir.getReturnValue();
		SeasonManager.Season season = SeasonManager.getCurrentSeason();

		if (season == SeasonManager.Season.AUTUMN) {
			// 秋: 葉っぱを赤・紅葉カラー（例: 0xD35400）に変更/ブレンド
			cir.setReturnValue(0xD35400);
		} else if (season == SeasonManager.Season.WINTER) {
			// 冬: 彩度を落とした枯れ木・雪色（例: 0x808070）に変更
			cir.setReturnValue(0x808070);
		}
	}
}
