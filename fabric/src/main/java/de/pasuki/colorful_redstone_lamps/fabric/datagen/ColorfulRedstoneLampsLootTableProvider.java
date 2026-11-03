package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ColorfulRedstoneLampsLootTableProvider implements DataProvider {
    private static final String MOD_ID = "colorful_redstone_lamps";

    private final FabricDataOutput output;

    public ColorfulRedstoneLampsLootTableProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        PackOutput.PathProvider lootTableProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table/blocks");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            String base = color.getName() + "_redstone_lamp";
            for (String suffix : List.of("", "_inverted", "_panel", "_panel_inverted", "_stairs", "_stairs_inverted")) {
                futures.add(saveLoot(cachedOutput, lootTableProvider, base + suffix, false));
            }
            // A double slab is two slabs and drops accordingly.
            for (String suffix : List.of("_slab", "_slab_inverted")) {
                futures.add(saveLoot(cachedOutput, lootTableProvider, base + suffix, true));
            }
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

    private static CompletableFuture<?> saveLoot(CachedOutput cachedOutput, PackOutput.PathProvider lootTableProvider,
                                                 String name, boolean slab) {
        return saveJson(cachedOutput, lootTableProvider.json(id(name)), selfDropLootTableJson(name, slab));
    }

    private static JsonObject selfDropLootTableJson(String blockPath, boolean slab) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);

        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", MOD_ID + ":" + blockPath);
        if (slab) {
            entry.add("functions", doubleSlabCount(blockPath));
        }
        entries.add(entry);

        pool.add("entries", entries);
        pools.add(pool);

        root.add("pools", pools);
        return root;
    }

    private static JsonArray doubleSlabCount(String blockPath) {
        JsonObject properties = new JsonObject();
        properties.addProperty("type", "double");

        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:block_state_property");
        condition.addProperty("block", MOD_ID + ":" + blockPath);
        condition.add("properties", properties);

        JsonArray conditions = new JsonArray();
        conditions.add(condition);

        JsonObject function = new JsonObject();
        function.addProperty("function", "minecraft:set_count");
        function.addProperty("count", 2.0);
        function.addProperty("add", true);
        function.add("conditions", conditions);

        JsonArray functions = new JsonArray();
        functions.add(function);
        return functions;
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
