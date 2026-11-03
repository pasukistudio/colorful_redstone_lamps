package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import java.util.Map;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import dev.architectury.registry.registries.RegistrySupplier;
import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public final class ColorfulRedstoneLampsBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> ANY_LAMP =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "any_lamp"));
    public static final TagKey<Block> LAMPS =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "redstone_lamps"));
    public static final TagKey<Block> INVERTED_LAMPS =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "inverted_redstone_lamps"));

    public ColorfulRedstoneLampsBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (DyeColor color : DyeColor.values()) {
            for (var group : List.of(ModBlocks.LAMPS, ModBlocks.LAMP_PANELS, ModBlocks.LAMP_SLABS, ModBlocks.LAMP_STAIRS)) {
                tag(LAMPS).add(blockKey(group, color));
            }
            for (var group : List.of(ModBlocks.INVERTED_LAMPS, ModBlocks.INVERTED_LAMP_PANELS, ModBlocks.INVERTED_LAMP_SLABS, ModBlocks.INVERTED_LAMP_STAIRS)) {
                tag(INVERTED_LAMPS).add(blockKey(group, color));
            }
        }

        tag(ANY_LAMP).addTag(LAMPS).addTag(INVERTED_LAMPS);
    }

    private static ResourceKey<Block> blockKey(Map<DyeColor, RegistrySupplier<Block>> blocks, DyeColor color) {
        return ResourceKey.create(Registries.BLOCK, blocks.get(color).getId());
    }
}
