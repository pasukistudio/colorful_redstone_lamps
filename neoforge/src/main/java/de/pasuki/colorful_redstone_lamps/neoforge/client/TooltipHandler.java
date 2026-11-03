package de.pasuki.colorful_redstone_lamps.neoforge.client;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = ColorfulRedstoneLamps.MOD_ID, value = Dist.CLIENT)
public final class TooltipHandler {
    private TooltipHandler() {}

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        if (stack.is(ModTags.Items.LAMPS)) {
            event.getToolTip().add(Component.translatable("tooltip.colorful_redstone_lamps.lamp.normal").withStyle(ChatFormatting.GRAY));
        } else if (stack.is(ModTags.Items.INVERTED_LAMPS)) {
            event.getToolTip().add(Component.translatable("tooltip.colorful_redstone_lamps.lamp.inverted").withStyle(ChatFormatting.GRAY));
        }

        if (stack.is(ModTags.Items.LAMP_PANELS) || stack.is(ModTags.Items.INVERTED_LAMP_PANELS)) {
            event.getToolTip().add(Component.translatable("tooltip.colorful_redstone_lamps.lamp.panel.placement").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
