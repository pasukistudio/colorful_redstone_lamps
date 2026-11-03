package de.pasuki.colorful_redstone_lamps.fabric;

import net.fabricmc.api.ModInitializer;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;

public final class ColorfulRedstoneLampsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ColorfulRedstoneLamps.init();
    }
}
