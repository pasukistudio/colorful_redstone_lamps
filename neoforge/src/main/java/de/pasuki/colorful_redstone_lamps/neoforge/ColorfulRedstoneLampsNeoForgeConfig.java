package de.pasuki.colorful_redstone_lamps.neoforge;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.config.ModConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ColorfulRedstoneLampsNeoForgeConfig {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue SHOW_WELCOME_MESSAGE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        SHOW_WELCOME_MESSAGE = builder
                .comment("Show the welcome message when a player joins for the first time.")
                .translation("config.colorful_redstone_lamps.show_welcome_message")
                .define("showWelcomeMessage", true);
        SPEC = builder.build();
    }

    private ColorfulRedstoneLampsNeoForgeConfig() {
    }

    static void syncToCommonConfig() {
        ModConfig.setShowWelcomeMessage(SHOW_WELCOME_MESSAGE.get());
    }

    @EventBusSubscriber(modid = ColorfulRedstoneLamps.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class Events {
        private Events() {
        }

        @SubscribeEvent
        public static void onConfigLoaded(ModConfigEvent.Loading event) {
            if (event.getConfig().getSpec() == SPEC) {
                syncToCommonConfig();
            }
        }

        @SubscribeEvent
        public static void onConfigReloaded(ModConfigEvent.Reloading event) {
            if (event.getConfig().getSpec() == SPEC) {
                syncToCommonConfig();
            }
        }
    }
}
