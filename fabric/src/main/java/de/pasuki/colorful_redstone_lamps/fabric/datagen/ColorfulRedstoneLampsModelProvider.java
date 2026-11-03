package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
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
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ColorfulRedstoneLampsModelProvider implements DataProvider {
    private static final String MOD_ID = "colorful_redstone_lamps";

    private final FabricPackOutput output;

    public ColorfulRedstoneLampsModelProvider(FabricPackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        PackOutput.PathProvider blockStateProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        PackOutput.PathProvider blockModelProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        PackOutput.PathProvider itemModelProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
        PackOutput.PathProvider itemDefinitionProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            String base = color.getName() + "_redstone_lamp";

            String offTexture = "block/" + base;
            String onTexture = "block/" + base + "_on";

            futures.add(saveJson(cachedOutput, blockModelProvider.json(id(base + "_off")), cubeAllModel(offTexture)));
            futures.add(saveJson(cachedOutput, blockModelProvider.json(id(base + "_on")), cubeAllModel(onTexture)));
            addLampFiles(futures, cachedOutput, blockStateProvider, itemModelProvider, itemDefinitionProvider, base, lampBlockStateJson(base + "_off", base + "_on"));

            String panel = base + "_panel";
            futures.add(saveJson(cachedOutput, blockModelProvider.json(id(panel + "_off")), panelModel(offTexture)));
            futures.add(saveJson(cachedOutput, blockModelProvider.json(id(panel + "_on")), panelModel(onTexture)));
            addLampFiles(futures, cachedOutput, blockStateProvider, itemModelProvider, itemDefinitionProvider, panel, panelBlockStateJson(panel + "_off", panel + "_on"));

            String slab = base + "_slab";
            for (String state : List.of("off", "on")) {
                String texture = state.equals("on") ? onTexture : offTexture;
                futures.add(saveJson(cachedOutput, blockModelProvider.json(id(slab + "_" + state)), texturedModel("minecraft:block/slab", texture)));
                futures.add(saveJson(cachedOutput, blockModelProvider.json(id(slab + "_top_" + state)), texturedModel("minecraft:block/slab_top", texture)));
            }
            addLampFiles(futures, cachedOutput, blockStateProvider, itemModelProvider, itemDefinitionProvider, slab, slabBlockStateJson(slab, base));

            String stairs = base + "_stairs";
            for (String state : List.of("off", "on")) {
                String texture = state.equals("on") ? onTexture : offTexture;
                futures.add(saveJson(cachedOutput, blockModelProvider.json(id(stairs + "_" + state)), texturedModel("minecraft:block/stairs", texture)));
                futures.add(saveJson(cachedOutput, blockModelProvider.json(id(stairs + "_inner_" + state)), texturedModel("minecraft:block/inner_stairs", texture)));
                futures.add(saveJson(cachedOutput, blockModelProvider.json(id(stairs + "_outer_" + state)), texturedModel("minecraft:block/outer_stairs", texture)));
            }
            addLampFiles(futures, cachedOutput, blockStateProvider, itemModelProvider, itemDefinitionProvider, stairs, stairsBlockStateJson(stairs));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    /** Item model definition (required since 1.21.2) pointing at the item model of the same name. */
    private static JsonObject itemDefinition(String name) {
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", MOD_ID + ":item/" + name);
        JsonObject root = new JsonObject();
        root.add("model", model);
        return root;
    }

    /** Writes blockstate and item model for a lamp and its inverted variant, which share the off/on models. */
    private static void addLampFiles(List<CompletableFuture<?>> futures, CachedOutput cachedOutput,
                                     PackOutput.PathProvider blockStateProvider, PackOutput.PathProvider itemModelProvider,
                                     PackOutput.PathProvider itemDefinitionProvider,
                                     String base, JsonObject blockState) {
        futures.add(saveJson(cachedOutput, blockStateProvider.json(id(base)), blockState));
        futures.add(saveJson(cachedOutput, blockStateProvider.json(id(base + "_inverted")), blockState));
        futures.add(saveJson(cachedOutput, itemModelProvider.json(id(base)), itemParentModel("block/" + base + "_off")));
        futures.add(saveJson(cachedOutput, itemModelProvider.json(id(base + "_inverted")), itemParentModel("block/" + base + "_on")));
        futures.add(saveJson(cachedOutput, itemDefinitionProvider.json(id(base)), itemDefinition(base)));
        futures.add(saveJson(cachedOutput, itemDefinitionProvider.json(id(base + "_inverted")), itemDefinition(base + "_inverted")));
    }

    @Override
    public String getName() {
        return "Colorful Redstone Lamps Models";
    }

    private static CompletableFuture<?> saveJson(CachedOutput cachedOutput, Path path, JsonObject json) {
        return DataProvider.saveStable(cachedOutput, json, path);
    }

    private static JsonObject cubeAllModel(String texturePath) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:block/cube_all");

        JsonObject textures = new JsonObject();
        textures.addProperty("all", MOD_ID + ":" + texturePath);
        json.add("textures", textures);

        return json;
    }

    private static JsonObject lampBlockStateJson(String offModelName, String onModelName) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();

        JsonObject off = new JsonObject();
        off.addProperty("model", MOD_ID + ":block/" + offModelName);
        variants.add("lit=false", off);

        JsonObject on = new JsonObject();
        on.addProperty("model", MOD_ID + ":block/" + onModelName);
        variants.add("lit=true", on);

        root.add("variants", variants);
        return root;
    }

    /** A vanilla slab/stairs parent model with the lamp texture on every side. */
    private static JsonObject texturedModel(String parent, String texturePath) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", parent);

        JsonObject textures = new JsonObject();
        for (String side : List.of("bottom", "top", "side")) {
            textures.addProperty(side, MOD_ID + ":" + texturePath);
        }
        json.add("textures", textures);
        return json;
    }

    /** The double slab reuses the full block model of the lamp. */
    private static JsonObject slabBlockStateJson(String slab, String lamp) {
        JsonObject variants = new JsonObject();
        for (String state : List.of("off", "on")) {
            String lit = "lit=" + state.equals("on");
            addVariant(variants, "type=bottom," + lit, slab + "_" + state, 0, 0, false);
            addVariant(variants, "type=top," + lit, slab + "_top_" + state, 0, 0, false);
            addVariant(variants, "type=double," + lit, lamp + "_" + state, 0, 0, false);
        }
        JsonObject root = new JsonObject();
        root.add("variants", variants);
        return root;
    }

    private static JsonObject stairsBlockStateJson(String stairs) {
        List<String> facings = List.of("east", "south", "west", "north");
        JsonObject variants = new JsonObject();
        for (String state : List.of("off", "on")) {
            for (int i = 0; i < facings.size(); i++) {
                int yaw = i * 90;
                for (String half : List.of("bottom", "top")) {
                    int x = half.equals("top") ? 180 : 0;
                    for (String shape : List.of("straight", "inner_left", "inner_right", "outer_left", "outer_right")) {
                        boolean left = shape.endsWith("left");
                        // Mirrors the vanilla stair rotations: left corners turn back a quarter on the bottom half, right corners forward on the top half.
                        int shapeYaw = shape.equals("straight") ? yaw
                                : half.equals("bottom") ? (left ? yaw + 270 : yaw)
                                : (left ? yaw : yaw + 90);
                        String suffix = shape.startsWith("inner") ? "_inner_" : shape.startsWith("outer") ? "_outer_" : "_";
                        addVariant(variants,
                                "facing=" + facings.get(i) + ",half=" + half + ",shape=" + shape + ",lit=" + state.equals("on"),
                                stairs + suffix + state, x, shapeYaw % 360, true);
                    }
                }
            }
        }
        JsonObject root = new JsonObject();
        root.add("variants", variants);
        return root;
    }

    private static void addVariant(JsonObject variants, String key, String modelName, int xRotation, int yRotation, boolean uvlock) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", MOD_ID + ":block/" + modelName);
        if (xRotation != 0) {
            variant.addProperty("x", xRotation);
        }
        if (yRotation != 0) {
            variant.addProperty("y", yRotation);
        }
        if (uvlock) {
            variant.addProperty("uvlock", true);
        }
        variants.add(key, variant);
    }

    private static JsonObject panelModel(String texturePath) {
        JsonObject root = new JsonObject();
        root.addProperty("ambientocclusion", false);

        JsonObject textures = new JsonObject();
        textures.addProperty("all", MOD_ID + ":" + texturePath);
        textures.addProperty("particle", MOD_ID + ":" + texturePath);
        root.add("textures", textures);

        JsonObject faces = new JsonObject();
        for (String direction : List.of("north", "south", "east", "west", "up", "down")) {
            JsonObject face = new JsonObject();
            face.addProperty("texture", "#all");
            faces.add(direction, face);
        }

        // Built for facing=north (2 pixels deep at the south edge of the block); the blockstate rotates it.
        JsonObject element = new JsonObject();
        element.add("from", vector(0, 0, 14));
        element.add("to", vector(16, 16, 16));
        element.add("faces", faces);

        JsonArray elements = new JsonArray();
        elements.add(element);
        root.add("elements", elements);
        return root;
    }

    private static JsonObject panelBlockStateJson(String offModelName, String onModelName) {
        JsonObject variants = new JsonObject();
        for (boolean lit : new boolean[]{false, true}) {
            String model = lit ? onModelName : offModelName;
            addPanelVariant(variants, "down", lit, model, 90, 0);
            addPanelVariant(variants, "up", lit, model, 270, 0);
            addPanelVariant(variants, "north", lit, model, 0, 0);
            addPanelVariant(variants, "south", lit, model, 0, 180);
            addPanelVariant(variants, "east", lit, model, 0, 90);
            addPanelVariant(variants, "west", lit, model, 0, 270);
        }
        JsonObject root = new JsonObject();
        root.add("variants", variants);
        return root;
    }

    private static void addPanelVariant(JsonObject variants, String facing, boolean lit, String modelName, int xRotation, int yRotation) {
        addVariant(variants, "facing=" + facing + ",lit=" + lit, modelName, xRotation, yRotation, false);
    }

    private static JsonArray vector(int x, int y, int z) {
        JsonArray vector = new JsonArray();
        vector.add(x);
        vector.add(y);
        vector.add(z);
        return vector;
    }

    private static JsonObject itemParentModel(String parentPath) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", MOD_ID + ":" + parentPath);
        return json;
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
