package de.pasuki.colorful_redstone_lamps.registry;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.EnumMap;
import java.util.Map;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ColorfulRedstoneLamps.MOD_ID, Registries.ITEM);

    public static final Map<DyeColor, RegistrySupplier<Item>> LAMP_ITEMS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Item>> INVERTED_LAMP_ITEMS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Item>> LAMP_PANEL_ITEMS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Item>> INVERTED_LAMP_PANEL_ITEMS =
            new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, RegistrySupplier<Item>> LAMP_SLAB_ITEMS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Item>> INVERTED_LAMP_SLAB_ITEMS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Item>> LAMP_STAIR_ITEMS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Item>> INVERTED_LAMP_STAIR_ITEMS =
            new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String base = color.getName() + "_redstone_lamp";
            registerBlockItem(LAMP_ITEMS, color, base, ModBlocks.LAMPS);
            registerBlockItem(INVERTED_LAMP_ITEMS, color, base + "_inverted", ModBlocks.INVERTED_LAMPS);
            registerBlockItem(LAMP_PANEL_ITEMS, color, base + "_panel", ModBlocks.LAMP_PANELS);
            registerBlockItem(INVERTED_LAMP_PANEL_ITEMS, color, base + "_panel_inverted", ModBlocks.INVERTED_LAMP_PANELS);
            registerBlockItem(LAMP_SLAB_ITEMS, color, base + "_slab", ModBlocks.LAMP_SLABS);
            registerBlockItem(INVERTED_LAMP_SLAB_ITEMS, color, base + "_slab_inverted", ModBlocks.INVERTED_LAMP_SLABS);
            registerBlockItem(LAMP_STAIR_ITEMS, color, base + "_stairs", ModBlocks.LAMP_STAIRS);
            registerBlockItem(INVERTED_LAMP_STAIR_ITEMS, color, base + "_stairs_inverted", ModBlocks.INVERTED_LAMP_STAIRS);
        }
    }

    private static void registerBlockItem(Map<DyeColor, RegistrySupplier<Item>> items, DyeColor color, String name,
                                          Map<DyeColor, RegistrySupplier<Block>> blocks) {
        items.put(color, ITEMS.register(name, () -> new BlockItem(blocks.get(color).get(), new Item.Properties())));
    }

    public static void register() {
        ITEMS.register();
    }
}
