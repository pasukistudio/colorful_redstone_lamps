package de.pasuki.colorful_redstone_lamps.fabric;

import de.pasuki.colorful_redstone_lamps.message.WelcomeMessageHandler;
import net.fabricmc.api.ModInitializer;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public final class ColorfulRedstoneLampsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ColorfulRedstoneLampsFabricConfig.load();
        ColorfulRedstoneLamps.init();
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                WelcomeMessageHandler.onPlayerJoined(handler.getPlayer())
        );
    }
}
