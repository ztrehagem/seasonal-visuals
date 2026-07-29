package dev.ztrehagem.seasonalvisuals.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class SeasonalVisualsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("seasonalvisuals");

	@Override
	public void onInitializeClient() {
		LOGGER.info("SeasonalVisuals initialized on Client Side!");

		ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
	}

	private void onClientTick(Minecraft client) {
		if (client.level == null) return;

		boolean seasonChanged = SeasonManager.updateSeason(client.level);
		if (seasonChanged) {
			onSeasonChanged(client, SeasonManager.getCurrentSeason());
		}
	}

	private void onSeasonChanged(Minecraft client, Season newSeason) {
		LOGGER.info("Season changed to {}. Reloading chunk renderers...", newSeason);
		if (client.levelExtractor != null) {
			client.levelExtractor.allChanged();
		}
	}
}


