package dev.ztrehagem.seasonalvisuals.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

public class SeasonalVisualsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("seasonalvisuals");

	@Override
	public void onInitializeClient() {
		LOGGER.info("SeasonalVisuals initialized on Client Side!");
	}
}
