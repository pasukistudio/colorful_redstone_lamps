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
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ColorfulRedstoneLampsRecipeProvider extends FabricRecipeProvider {

    public ColorfulRedstoneLampsRecipeProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {

            @Override
            public void buildRecipes() {
                for (DyeColor color : DyeColor.values()) {
                    String base = color.getName() + "_redstone_lamp";

                    ItemLike normalLamp = ModBlocks.LAMPS.get(color).get();
                    ItemLike invertedLamp = ModBlocks.INVERTED_LAMPS.get(color).get();
                    Item dye = dyeFor(color);

                    // Vanilla lamp + dye -> colored lamp
                    shapeless(RecipeCategory.REDSTONE, normalLamp, 1)
                            .requires(Items.REDSTONE_LAMP)
                            .requires(dye)
                            .unlockedBy("has_redstone_lamp", has(Items.REDSTONE_LAMP))
                            .unlockedBy("has_dye_" + color.getName(), has(dye))
                            .save(recipeOutput, id("craft/" + base));

                    // Colored lamp -> inverted lamp
                    shapeless(RecipeCategory.REDSTONE, invertedLamp, 1)
                            .requires(normalLamp)
                            .unlockedBy("has_" + base, has(normalLamp))
                            .save(recipeOutput, id("invert/" + base + "_to_inverted"));

                    // Inverted lamp -> normal lamp
                    shapeless(RecipeCategory.REDSTONE, normalLamp, 1)
                            .requires(invertedLamp)
                            .unlockedBy("has_" + base + "_inverted", has(invertedLamp))
                            .save(recipeOutput, id("invert/" + base + "_to_normal"));
                }
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
