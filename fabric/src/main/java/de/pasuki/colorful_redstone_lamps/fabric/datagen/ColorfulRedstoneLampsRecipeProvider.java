package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public final class ColorfulRedstoneLampsRecipeProvider extends FabricRecipeProvider {
    public ColorfulRedstoneLampsRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput out) {
        for (DyeColor color : DyeColor.values()) {
            String base = color.getName() + "_redstone_lamp";

            ItemLike normalLamp = ModBlocks.LAMPS.get(color).get();
            ItemLike invertedLamp = ModBlocks.INVERTED_LAMPS.get(color).get();
            ItemLike normalPanel = ModBlocks.LAMP_PANELS.get(color).get();
            ItemLike invertedPanel = ModBlocks.INVERTED_LAMP_PANELS.get(color).get();
            Item dye = dyeFor(color);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, normalLamp, 1)
                    .requires(Items.REDSTONE_LAMP)
                    .requires(dye)
                    .unlockedBy("has_redstone_lamp", has(Items.REDSTONE_LAMP))
                    .unlockedBy("has_dye_" + color.getName(), has(dye))
                    .save(out, id("craft/" + base));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, invertedLamp, 1)
                    .requires(normalLamp)
                    .unlockedBy("has_" + base, has(normalLamp))
                    .save(out, id("invert/" + base + "_to_inverted"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, normalLamp, 1)
                    .requires(invertedLamp)
                    .unlockedBy("has_" + base + "_inverted", has(invertedLamp))
                    .save(out, id("invert/" + base + "_to_normal"));

            // 2 lamps are cut into 12 panels, 6 panels are assembled back into a lamp.
            cutIntoPanels(out, normalLamp, normalPanel, "has_" + base, "panel/craft/" + base + "_to_panels");
            cutIntoPanels(out, invertedLamp, invertedPanel, "has_" + base + "_inverted", "panel/craft/" + base + "_inverted_to_panels");
            assembleLamp(out, normalPanel, normalLamp, "has_" + base + "_panel", "panel/assemble/" + base);
            assembleLamp(out, invertedPanel, invertedLamp, "has_" + base + "_panel_inverted", "panel/assemble/" + base + "_inverted");

            addInvertRecipes(out, "panel", base, "_panel", normalPanel, invertedPanel);

            ItemLike normalSlab = ModBlocks.LAMP_SLABS.get(color).get();
            ItemLike invertedSlab = ModBlocks.INVERTED_LAMP_SLABS.get(color).get();
            ItemLike normalStairs = ModBlocks.LAMP_STAIRS.get(color).get();
            ItemLike invertedStairs = ModBlocks.INVERTED_LAMP_STAIRS.get(color).get();

            // Same yields as vanilla: 3 lamps make 6 slabs, 6 lamps make 4 stairs.
            slabs(out, normalLamp, normalSlab, "has_" + base, "slab/craft/" + base);
            slabs(out, invertedLamp, invertedSlab, "has_" + base + "_inverted", "slab/craft/" + base + "_inverted");
            stairs(out, normalLamp, normalStairs, "has_" + base, "stairs/craft/" + base);
            stairs(out, invertedLamp, invertedStairs, "has_" + base + "_inverted", "stairs/craft/" + base + "_inverted");
            addInvertRecipes(out, "slab", base, "_slab", normalSlab, invertedSlab);
            addInvertRecipes(out, "stairs", base, "_stairs", normalStairs, invertedStairs);
        }
    }

    private void addInvertRecipes(RecipeOutput out, String folder, String base, String suffix, ItemLike normal, ItemLike inverted) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, inverted)
                .requires(normal)
                .unlockedBy("has_" + base + suffix, has(normal))
                .save(out, id(folder + "/invert/" + base + "_to_inverted"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, normal)
                .requires(inverted)
                .unlockedBy("has_" + base + suffix + "_inverted", has(inverted))
                .save(out, id(folder + "/invert/" + base + "_inverted_to_normal"));
    }

    private void slabs(RecipeOutput out, ItemLike lamp, ItemLike slab, String unlockName, String recipeId) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, slab, 6)
                .pattern("LLL")
                .define('L', lamp)
                .unlockedBy(unlockName, has(lamp))
                .save(out, id(recipeId));
    }

    private void stairs(RecipeOutput out, ItemLike lamp, ItemLike stairs, String unlockName, String recipeId) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, stairs, 4)
                .pattern("L  ")
                .pattern("LL ")
                .pattern("LLL")
                .define('L', lamp)
                .unlockedBy(unlockName, has(lamp))
                .save(out, id(recipeId));
    }

    private void cutIntoPanels(RecipeOutput out, ItemLike lamp, ItemLike panel, String unlockName, String recipeId) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, panel, 12)
                .pattern("LL")
                .define('L', lamp)
                .unlockedBy(unlockName, has(lamp))
                .save(out, id(recipeId));
    }

    private void assembleLamp(RecipeOutput out, ItemLike panel, ItemLike lamp, String unlockName, String recipeId) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, lamp)
                .pattern("PPP")
                .pattern("PPP")
                .define('P', panel)
                .unlockedBy(unlockName, has(panel))
                .save(out, id(recipeId));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, path);
    }

    private static Item dyeFor(DyeColor color) {
        return switch (color) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }
}
