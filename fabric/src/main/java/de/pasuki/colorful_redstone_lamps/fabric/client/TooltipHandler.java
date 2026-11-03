package de.pasuki.colorful_redstone_lamps.fabric.client;

import de.pasuki.colorful_redstone_lamps.registry.ModTags;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class TooltipHandler {
    private TooltipHandler() {}

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.isEmpty()) {
                return;
            }

            if (stack.is(ModTags.Items.LAMPS)) {
                lines.add(Component.translatable("tooltip.colorful_redstone_lamps.lamp.normal").withStyle(ChatFormatting.GRAY));
            } else if (stack.is(ModTags.Items.INVERTED_LAMPS)) {
                lines.add(Component.translatable("tooltip.colorful_redstone_lamps.lamp.inverted").withStyle(ChatFormatting.GRAY));
            }

            if (stack.is(ModTags.Items.LAMP_PANELS) || stack.is(ModTags.Items.INVERTED_LAMP_PANELS)) {
                lines.add(Component.translatable("tooltip.colorful_redstone_lamps.lamp.panel.placement").withStyle(ChatFormatting.DARK_GRAY));
            }
        });
    }
}
