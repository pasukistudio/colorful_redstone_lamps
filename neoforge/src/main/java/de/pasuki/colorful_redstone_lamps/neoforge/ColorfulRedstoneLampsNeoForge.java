package de.pasuki.colorful_redstone_lamps.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;

@Mod(ColorfulRedstoneLamps.MOD_ID)
public final class ColorfulRedstoneLampsNeoForge {
    public ColorfulRedstoneLampsNeoForge(ModContainer container) {
        if (FMLEnvironment.getDist().isClient()) {
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
        ColorfulRedstoneLamps.init();
    }
}
