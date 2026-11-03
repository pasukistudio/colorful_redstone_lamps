package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import java.util.List;
import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ColorfulRedstoneLampsItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> ANY_LAMP =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "any_lamp"));
    public static final TagKey<Item> LAMPS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "redstone_lamps"));
    public static final TagKey<Item> INVERTED_LAMPS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "inverted_redstone_lamps"));
    public static final TagKey<Item> LAMP_PANELS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "redstone_lamp_panels"));
    public static final TagKey<Item> INVERTED_LAMP_PANELS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, "inverted_redstone_lamp_panels"));

    public ColorfulRedstoneLampsItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var lampsTag = valueLookupBuilder(LAMPS);
        var invertedTag = valueLookupBuilder(INVERTED_LAMPS);
        var anyLampTag = valueLookupBuilder(ANY_LAMP);
        var panelsTag = valueLookupBuilder(LAMP_PANELS);
        var invertedPanelsTag = valueLookupBuilder(INVERTED_LAMP_PANELS);

        for (DyeColor color : DyeColor.values()) {
            for (var group : List.of(ModBlocks.LAMPS, ModBlocks.LAMP_PANELS, ModBlocks.LAMP_SLABS, ModBlocks.LAMP_STAIRS)) {
                lampsTag.add(group.get(color).get().asItem());
            }
            for (var group : List.of(ModBlocks.INVERTED_LAMPS, ModBlocks.INVERTED_LAMP_PANELS, ModBlocks.INVERTED_LAMP_SLABS, ModBlocks.INVERTED_LAMP_STAIRS)) {
                invertedTag.add(group.get(color).get().asItem());
            }
            panelsTag.add(ModBlocks.LAMP_PANELS.get(color).get().asItem());
            invertedPanelsTag.add(ModBlocks.INVERTED_LAMP_PANELS.get(color).get().asItem());
        }

        anyLampTag.addTag(LAMPS);
        anyLampTag.addTag(INVERTED_LAMPS);
    }
}
