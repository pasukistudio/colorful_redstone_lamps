package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import de.pasuki.colorful_redstone_lamps.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class ColorfulRedstoneLampsRecipeProvider extends FabricRecipeProvider {

    public ColorfulRedstoneLampsRecipeProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(
            HolderLookup.Provider provider,
            BootstrapContext<Recipe<?>> recipes,
            BootstrapContext<Advancement> advancements
    ) {
        return new RecipeProvider(recipes, advancements) {

            @Override
            public void buildRecipes() {
                RecipeOutput recipeOutput = new RecipeOutput26_3Fix(output);

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
        return Items.DYE.pick(color);
    }

    /**
     * Minecraft 26.3 creates recipe-advancement requirements before the custom
     * unlock criteria are added. Deferring that one call keeps the requirements
     * synchronized with all criteria when the advancement is built.
     */
    private static final class RecipeOutput26_3Fix implements RecipeOutput {
        private final RecipeOutput delegate;

        private RecipeOutput26_3Fix(RecipeOutput delegate) {
            this.delegate = delegate;
        }

        @Override
        public void accept(ResourceKey<Recipe<?>> key, Recipe<?> recipe, AdvancementHolder advancement) {
            delegate.accept(key, recipe, advancement);
        }

        @Override
        public Advancement.Builder advancement() {
            return new DeferredRequirementsAdvancementBuilder();
        }

        @Override
        public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) {
            return delegate.lookup(key);
        }

        @Override
        public <S> Stream<Holder.Reference<S>> listContextElements(ResourceKey<? extends Registry<? extends S>> key) {
            return delegate.listContextElements(key);
        }
    }

    private static final class DeferredRequirementsAdvancementBuilder extends Advancement.Builder {
        @Override
        public Advancement.Builder requirements(AdvancementRequirements.Strategy strategy) {
            return this;
        }

        @Override
        public AdvancementHolder build(Identifier id) {
            super.requirements(AdvancementRequirements.Strategy.OR);
            return super.build(id);
        }
    }

    @Override
    public String getName() {
        return "Colorful Redstone Lamps Recipes";
    }
}
