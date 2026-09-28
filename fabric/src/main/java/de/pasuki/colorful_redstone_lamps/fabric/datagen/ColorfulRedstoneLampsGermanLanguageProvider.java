package de.pasuki.colorful_redstone_lamps.fabric.datagen;

import de.pasuki.colorful_redstone_lamps.datagen.ModLanguageEntries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ColorfulRedstoneLampsGermanLanguageProvider extends FabricLanguageProvider {
    public ColorfulRedstoneLampsGermanLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "de_de", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        for (Map.Entry<String, String> entry : ModLanguageEntries.germanDe().entrySet()) {
            translationBuilder.add(entry.getKey(), entry.getValue());
        }
    }
}
