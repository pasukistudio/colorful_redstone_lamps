package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public final class ColorfulRedstoneLampsItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> ANY_LAMP =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "any_lamp"));
    public static final TagKey<Item> LAMPS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "redstone_lamps"));
    public static final TagKey<Item> INVERTED_LAMPS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "inverted_redstone_lamps"));

    public ColorfulRedstoneLampsItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var lampsTag = builder(LAMPS);
        var invertedTag = builder(INVERTED_LAMPS);
        var anyLampTag = builder(ANY_LAMP);

        for (DyeColor color : DyeColor.values()) {
            lampsTag.add(ResourceKey.create(Registries.ITEM, ModBlocks.LAMPS.get(color).getId()));
            invertedTag.add(ResourceKey.create(Registries.ITEM, ModBlocks.INVERTED_LAMPS.get(color).getId()));
        }

        anyLampTag.addTag(LAMPS);
        anyLampTag.addTag(INVERTED_LAMPS);
    }
}
