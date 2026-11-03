package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ColorfulRedstoneLampsRecipeProvider extends FabricRecipeProvider {

    public ColorfulRedstoneLampsRecipeProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {

            @Override
            public void buildRecipes() {
                for (DyeColor color : DyeColor.values()) {
                    addColorRecipes(color);
                }
            }

            private void addColorRecipes(DyeColor color) {
                String base = color.getName() + "_redstone_lamp";

                ItemLike normalLamp = ModBlocks.LAMPS.get(color).get();
                ItemLike invertedLamp = ModBlocks.INVERTED_LAMPS.get(color).get();
                ItemLike normalPanel = ModBlocks.LAMP_PANELS.get(color).get();
                ItemLike invertedPanel = ModBlocks.INVERTED_LAMP_PANELS.get(color).get();
                ItemLike normalSlab = ModBlocks.LAMP_SLABS.get(color).get();
                ItemLike invertedSlab = ModBlocks.INVERTED_LAMP_SLABS.get(color).get();
                ItemLike normalStairs = ModBlocks.LAMP_STAIRS.get(color).get();
                ItemLike invertedStairs = ModBlocks.INVERTED_LAMP_STAIRS.get(color).get();
                Item dye = dyeFor(color);

                // Vanilla lamp + dye -> colored lamp
                shapeless(RecipeCategory.REDSTONE, normalLamp, 1)
                        .requires(Items.REDSTONE_LAMP)
                        .requires(dye)
                        .unlockedBy("has_redstone_lamp", has(Items.REDSTONE_LAMP))
                        .unlockedBy("has_dye_" + color.getName(), has(dye))
                        .save(recipeOutput, id("craft/" + base));

                // Colored lamp <-> inverted lamp
                shapeless(RecipeCategory.REDSTONE, invertedLamp, 1)
                        .requires(normalLamp)
                        .unlockedBy("has_" + base, has(normalLamp))
                        .save(recipeOutput, id("invert/" + base + "_to_inverted"));

                shapeless(RecipeCategory.REDSTONE, normalLamp, 1)
                        .requires(invertedLamp)
                        .unlockedBy("has_" + base + "_inverted", has(invertedLamp))
                        .save(recipeOutput, id("invert/" + base + "_to_normal"));

                // 2 lamps are cut into 12 panels, 6 panels are assembled back into a lamp.
                cutIntoPanels(normalLamp, normalPanel, "has_" + base, "panel/craft/" + base + "_to_panels");
                cutIntoPanels(invertedLamp, invertedPanel, "has_" + base + "_inverted", "panel/craft/" + base + "_inverted_to_panels");
                assembleLamp(normalPanel, normalLamp, "has_" + base + "_panel", "panel/assemble/" + base);
                assembleLamp(invertedPanel, invertedLamp, "has_" + base + "_panel_inverted", "panel/assemble/" + base + "_inverted");
                addInvertRecipes("panel", base, "_panel", normalPanel, invertedPanel);

                // Same yields as vanilla: 3 lamps make 6 slabs, 6 lamps make 4 stairs.
                slabs(normalLamp, normalSlab, "has_" + base, "slab/craft/" + base);
                slabs(invertedLamp, invertedSlab, "has_" + base + "_inverted", "slab/craft/" + base + "_inverted");
                stairs(normalLamp, normalStairs, "has_" + base, "stairs/craft/" + base);
                stairs(invertedLamp, invertedStairs, "has_" + base + "_inverted", "stairs/craft/" + base + "_inverted");
                addInvertRecipes("slab", base, "_slab", normalSlab, invertedSlab);
                addInvertRecipes("stairs", base, "_stairs", normalStairs, invertedStairs);
            }

            private void addInvertRecipes(String folder, String base, String suffix, ItemLike normal, ItemLike inverted) {
                shapeless(RecipeCategory.REDSTONE, inverted, 1)
                        .requires(normal)
                        .unlockedBy("has_" + base + suffix, has(normal))
                        .save(recipeOutput, id(folder + "/invert/" + base + "_to_inverted"));

                shapeless(RecipeCategory.REDSTONE, normal, 1)
                        .requires(inverted)
                        .unlockedBy("has_" + base + suffix + "_inverted", has(inverted))
                        .save(recipeOutput, id(folder + "/invert/" + base + "_inverted_to_normal"));
            }

            private void slabs(ItemLike lamp, ItemLike slab, String unlockName, String recipeId) {
                shaped(RecipeCategory.REDSTONE, slab, 6)
                        .pattern("LLL")
                        .define('L', lamp)
                        .unlockedBy(unlockName, has(lamp))
                        .save(recipeOutput, id(recipeId));
            }

            private void stairs(ItemLike lamp, ItemLike stairs, String unlockName, String recipeId) {
                shaped(RecipeCategory.REDSTONE, stairs, 4)
                        .pattern("L  ")
                        .pattern("LL ")
                        .pattern("LLL")
                        .define('L', lamp)
                        .unlockedBy(unlockName, has(lamp))
                        .save(recipeOutput, id(recipeId));
            }

            private void cutIntoPanels(ItemLike lamp, ItemLike panel, String unlockName, String recipeId) {
                shaped(RecipeCategory.REDSTONE, panel, 12)
                        .pattern("LL")
                        .define('L', lamp)
                        .unlockedBy(unlockName, has(lamp))
                        .save(recipeOutput, id(recipeId));
            }

            private void assembleLamp(ItemLike panel, ItemLike lamp, String unlockName, String recipeId) {
                shaped(RecipeCategory.REDSTONE, lamp, 1)
                        .pattern("PPP")
                        .pattern("PPP")
                        .define('P', panel)
                        .unlockedBy(unlockName, has(panel))
                        .save(recipeOutput, id(recipeId));
            }

            private static ResourceKey<Recipe<?>> id(String path) {
                return ResourceKey.create(
                        Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, path)
                );
            }
        };
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

    @Override
    public String getName() {
        return "Colorful Redstone Lamps Recipes";
    }
}
