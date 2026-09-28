package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ColorfulRedstoneLampsLootTableProvider implements DataProvider {
    private static final String MOD_ID = "colorful_redstone_lamps";

    private final FabricPackOutput output;

    public ColorfulRedstoneLampsLootTableProvider(FabricPackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        PackOutput.PathProvider lootTableProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table/blocks");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            String base = color.getName() + "_redstone_lamp";
            futures.add(saveJson(cachedOutput, lootTableProvider.json(id(base)), selfDropLootTableJson(base)));
            futures.add(saveJson(cachedOutput, lootTableProvider.json(id(base + "_inverted")), selfDropLootTableJson(base + "_inverted")));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Colorful Redstone Lamps Loot Tables";
    }

    private static CompletableFuture<?> saveJson(CachedOutput cachedOutput, Path path, JsonObject json) {
        return DataProvider.saveStable(cachedOutput, json, path);
    }

    private static JsonObject selfDropLootTableJson(String blockPath) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);

        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", MOD_ID + ":" + blockPath);
        entries.add(entry);

        pool.add("entries", entries);
        pools.add(pool);

        root.add("pools", pools);
        return root;
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
