package dev.ztrehagem.seasonalvisuals.client.mixin;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ztrehagem.seasonalvisuals.client.Season;
import dev.ztrehagem.seasonalvisuals.client.SeasonManager;

@Mixin(Biome.class)
public class BiomeMixin {

	// 葉っぱ（Foliage）の色計算に介入
	@Inject(method = "getFoliageColor", at = @At("RETURN"), cancellable = true)
	private void modifyFoliageColor(CallbackInfoReturnable<Integer> cir) {
		Season season = SeasonManager.getCurrentSeason();
		int originalColor = cir.getReturnValue();
		int newColor = SeasonManager.getFoliageColor((Biome) (Object) this, season, originalColor);

		if (newColor != originalColor) {
			cir.setReturnValue(newColor);
		}
	}
}

