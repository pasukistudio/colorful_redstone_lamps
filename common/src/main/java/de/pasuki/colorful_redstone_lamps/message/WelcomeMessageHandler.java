package de.pasuki.colorful_redstone_lamps.message;

import de.pasuki.colorful_redstone_lamps.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class WelcomeMessageHandler {
    private static final String GITHUB_URL = "https://github.com/PasukiTV/colorful_redstone_lamps/issues";
    private static final String DISCORD_URL = "https://discord.gg/9y97PyeD6s";

    private WelcomeMessageHandler() {
    }

    public static void onPlayerJoined(ServerPlayer player) {
        if (!ModConfig.showWelcomeMessage()) {
            return;
        }

        ServerLevel overworld = player.server.overworld();
        WelcomeMessageData data = WelcomeMessageData.get(overworld);
        var playerId = player.getUUID();

        if (!data.wasShown(playerId)) {
            player.sendSystemMessage(
                    Component.translatable("colorful_redstone_lamps.welcome.title")
                            .withStyle(ChatFormatting.GRAY)
            );

            player.sendSystemMessage(
                    Component.translatable("colorful_redstone_lamps.welcome.feedback_intro")
                            .withStyle(ChatFormatting.DARK_GRAY)
                            .append(Component.literal(" "))
                            .append(link("colorful_redstone_lamps.welcome.feedback.github", GITHUB_URL))
                            .append(Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY))
                            .append(link("colorful_redstone_lamps.welcome.feedback.discord", DISCORD_URL))
            );

            data.setShown(playerId);
        }
    }

    private static Component link(String key, String url) {
        return Component.translatable(key)
                .withStyle(style -> style
                        .withColor(ChatFormatting.AQUA)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                );
    }
}
