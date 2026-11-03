package de.pasuki.colorful_redstone_lamps.registry;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.block.InvertedRedstoneLampBlock;
import de.pasuki.colorful_redstone_lamps.block.LampSlabBlock;
import de.pasuki.colorful_redstone_lamps.block.LampStairBlock;
import de.pasuki.colorful_redstone_lamps.block.WallMountedRedstoneLampBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ColorfulRedstoneLamps.MOD_ID, Registries.BLOCK);

    // Helper methods used by BlockBehavior.Properties.
    private static ToIntFunction<BlockState> litBlockEmission() {
        return state -> state.getValue(RedstoneLampBlock.LIT) ? 15 : 0;
    }

    private static boolean always(BlockState s, BlockGetter g, BlockPos p, EntityType<?> t) {
        return true;
    }

    private static BlockBehaviour.Properties baseProps(String name, DyeColor color) {
        return BlockBehaviour.Properties.of()
                .setId(blockKey(name))
                .mapColor(color.getMapColor())
                .strength(0.3F)
                .sound(SoundType.GLASS)
                .lightLevel(litBlockEmission())
                .isValidSpawn(ModBlocks::always);
    }

    // Lamp registries for all dye colors (normal and inverted variants).
    public static final Map<DyeColor, RegistrySupplier<Block>> LAMPS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> INVERTED_LAMPS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> LAMP_PANELS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> INVERTED_LAMP_PANELS =
            new EnumMap<>(DyeColor.class);

    public static final Map<DyeColor, RegistrySupplier<Block>> LAMP_SLABS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> INVERTED_LAMP_SLABS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> LAMP_STAIRS =
            new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> INVERTED_LAMP_STAIRS =
            new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String base = color.getName() + "_redstone_lamp";
            LAMPS.put(color, registerBlock(base, () -> new RedstoneLampBlock(baseProps(base, color))));
            INVERTED_LAMPS.put(color, registerBlock(base + "_inverted", () -> new InvertedRedstoneLampBlock(baseProps(base + "_inverted", color))));
            LAMP_PANELS.put(color, registerBlock(base + "_panel",
                    () -> new WallMountedRedstoneLampBlock(baseProps(base + "_panel", color).noOcclusion(), false)));
            INVERTED_LAMP_PANELS.put(color, registerBlock(base + "_panel_inverted",
                    () -> new WallMountedRedstoneLampBlock(baseProps(base + "_panel_inverted", color).noOcclusion(), true)));
            LAMP_SLABS.put(color, registerBlock(base + "_slab", () -> new LampSlabBlock(baseProps(base + "_slab", color), false)));
            INVERTED_LAMP_SLABS.put(color, registerBlock(base + "_slab_inverted", () -> new LampSlabBlock(baseProps(base + "_slab_inverted", color), true)));
            LAMP_STAIRS.put(color, registerBlock(base + "_stairs", () -> new LampStairBlock(baseProps(base + "_stairs", color), false)));
            INVERTED_LAMP_STAIRS.put(color, registerBlock(base + "_stairs_inverted", () -> new LampStairBlock(baseProps(base + "_stairs_inverted", color), true)));
        }
    }

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, name));
    }

    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static void register() {
        BLOCKS.register();
    }
}
