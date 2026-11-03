package de.pasuki.colorful_redstone_lamps.registry;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.level.ItemLike;

public final class ModCreativeTabs {
    private ModCreativeTabs() {}

    /** A visual palette: neutral shades first, then a warm-to-cool color gradient. */
    private static final List<DyeColor> COLOR_ORDER = List.of(
            DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK,
            DyeColor.BROWN, DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW,
            DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
            DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK
    );

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(ColorfulRedstoneLamps.MOD_ID, Registries.CREATIVE_MODE_TAB);

    private static void registerTabs() {
        TABS.register("0_normal_lamps", () -> createLampTab(
                "itemGroup.colorful_redstone_lamps.normal_lamps",
                () -> new ItemStack(ModItems.LAMP_ITEMS.get(DyeColor.RED).get()),
                0,
                ModItems.LAMP_ITEMS, ModItems.LAMP_PANEL_ITEMS, ModItems.LAMP_SLAB_ITEMS, ModItems.LAMP_STAIR_ITEMS
        ));

        TABS.register("1_inverted_lamps", () -> createLampTab(
                "itemGroup.colorful_redstone_lamps.inverted_lamps",
                () -> new ItemStack(ModItems.INVERTED_LAMP_ITEMS.get(DyeColor.RED).get()),
                1,
                ModItems.INVERTED_LAMP_ITEMS, ModItems.INVERTED_LAMP_PANEL_ITEMS,
                ModItems.INVERTED_LAMP_SLAB_ITEMS, ModItems.INVERTED_LAMP_STAIR_ITEMS
        ));
    }

    @SafeVarargs
    private static CreativeModeTab createLampTab(
            String translationKey,
            Supplier<ItemStack> icon,
            int column,
            Map<DyeColor, ? extends RegistrySupplier<? extends ItemLike>>... itemGroups
    ) {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, column)
                .icon(icon)
                .title(Component.translatable(translationKey))
                .displayItems((params, output) -> {
                    for (var items : itemGroups) {
                        addItems(output, items);
                    }
                })
                .build();
    }

    private static void addItems(CreativeModeTab.Output output,
                                 Map<DyeColor, ? extends RegistrySupplier<? extends ItemLike>> items) {
        for (DyeColor color : COLOR_ORDER) {
            RegistrySupplier<? extends ItemLike> item = items.get(color);
            if (item != null) {
                output.accept(item.get());
            }
        }
    }

    public static void register() {
        registerTabs();
        TABS.register();
    }
}
