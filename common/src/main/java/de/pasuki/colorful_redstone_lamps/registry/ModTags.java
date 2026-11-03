package de.pasuki.colorful_redstone_lamps.registry;

import de.pasuki.colorful_redstone_lamps.ColorfulRedstoneLamps;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    private ModTags() {}

    public static final class Items {
        public static final TagKey<Item> LAMPS = tag("redstone_lamps");
        public static final TagKey<Item> INVERTED_LAMPS = tag("inverted_redstone_lamps");
        public static final TagKey<Item> LAMP_PANELS = tag("redstone_lamp_panels");
        public static final TagKey<Item> INVERTED_LAMP_PANELS = tag("inverted_redstone_lamp_panels");

        private static TagKey<Item> tag(String path) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ColorfulRedstoneLamps.MOD_ID, path));
        }
    }
}
