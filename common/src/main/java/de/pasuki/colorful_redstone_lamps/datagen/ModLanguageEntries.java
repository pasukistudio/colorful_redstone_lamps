package de.pasuki.colorful_redstone_lamps.datagen;

import net.minecraft.world.item.DyeColor;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModLanguageEntries {
    private ModLanguageEntries() {
    }

    public static Map<String, String> englishUs() {
        Map<String, String> entries = new LinkedHashMap<>();

        for (DyeColor color : DyeColor.values()) {
            String colorName = toEnglishColorDisplayName(color);
            String baseId = color.getName() + "_redstone_lamp";

            putBlockAndItem(entries, baseId, colorName + " Redstone Lamp");
            putBlockAndItem(entries, baseId + "_inverted", colorName + " Redstone Lamp (Inverted)");
            putBlockAndItem(entries, baseId + "_panel", colorName + " Redstone Lamp Panel");
            putBlockAndItem(entries, baseId + "_panel_inverted", colorName + " Redstone Lamp Panel (Inverted)");
            putBlockAndItem(entries, baseId + "_slab", colorName + " Redstone Lamp Slab");
            putBlockAndItem(entries, baseId + "_slab_inverted", colorName + " Redstone Lamp Slab (Inverted)");
            putBlockAndItem(entries, baseId + "_stairs", colorName + " Redstone Lamp Stairs");
            putBlockAndItem(entries, baseId + "_stairs_inverted", colorName + " Redstone Lamp Stairs (Inverted)");
        }

        entries.put("itemGroup.colorful_redstone_lamps.normal_lamps", "Colorful Redstone Lamps");
        entries.put("itemGroup.colorful_redstone_lamps.inverted_lamps", "Inverted Colorful Redstone Lamps");
        entries.put("tooltip.colorful_redstone_lamps.lamp.normal",   "Turns on with a redstone signal.");
        entries.put("tooltip.colorful_redstone_lamps.lamp.inverted", "Turns off with a redstone signal.");
        entries.put("tooltip.colorful_redstone_lamps.lamp.panel.placement", "Can be placed on walls, floors, and ceilings.");
        return entries;
    }

    public static Map<String, String> germanDe() {
        Map<String, String> entries = new LinkedHashMap<>();

        for (DyeColor color : DyeColor.values()) {
            String colorName = toGermanColorDisplayName(color);
            String panelColorName = toGermanPanelColorDisplayName(color);
            String baseId = color.getName() + "_redstone_lamp";

            putBlockAndItem(entries, baseId, colorName + " Redstone-Lampe");
            putBlockAndItem(entries, baseId + "_inverted", colorName + " invertierte Redstone-Lampe");
            putBlockAndItem(entries, baseId + "_panel", panelColorName + " Redstone-Lampenpanel");
            putBlockAndItem(entries, baseId + "_panel_inverted", panelColorName + " invertiertes Redstone-Lampenpanel");
            putBlockAndItem(entries, baseId + "_slab", colorName + " Redstone-Lampenplatte");
            putBlockAndItem(entries, baseId + "_slab_inverted", colorName + " invertierte Redstone-Lampenplatte");
            putBlockAndItem(entries, baseId + "_stairs", colorName + " Redstone-Lampentreppe");
            putBlockAndItem(entries, baseId + "_stairs_inverted", colorName + " invertierte Redstone-Lampentreppe");
        }

        entries.put("itemGroup.colorful_redstone_lamps.normal_lamps", "Bunte Redstone-Lampen");
        entries.put("itemGroup.colorful_redstone_lamps.inverted_lamps", "Bunte Redstone-Lampen Invertiert");
        entries.put("tooltip.colorful_redstone_lamps.lamp.normal",   "Schaltet sich bei Redstone-Signal ein.");
        entries.put("tooltip.colorful_redstone_lamps.lamp.inverted", "Schaltet sich bei Redstone-Signal aus.");
        entries.put("tooltip.colorful_redstone_lamps.lamp.panel.placement", "Kann an Wänden, Böden und Decken angebracht werden.");
        return entries;
    }

    private static void putBlockAndItem(Map<String, String> entries, String id, String name) {
        entries.put("block.colorful_redstone_lamps." + id, name);
        entries.put("item.colorful_redstone_lamps." + id, name);
    }

    private static String toEnglishColorDisplayName(DyeColor color) {
        String[] words = color.getName().split("_");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
                if (i < words.length - 1) {
                    builder.append(' ');
                }
            }
        }

        return builder.toString();
    }

    private static String toGermanColorDisplayName(DyeColor color) {
        return switch (color) {
            case WHITE -> "Weiße";
            case LIGHT_GRAY -> "Hellgraue";
            case GRAY -> "Graue";
            case BLACK -> "Schwarze";
            case BROWN -> "Braune";
            case RED -> "Rote";
            case ORANGE -> "Orange";
            case YELLOW -> "Gelbe";
            case LIME -> "Hellgrüne";
            case GREEN -> "Grüne";
            case CYAN -> "Türkise";
            case LIGHT_BLUE -> "Hellblaue";
            case BLUE -> "Blaue";
            case PURPLE -> "Violette";
            case MAGENTA -> "Magenta";
            case PINK -> "Rosa";
        };
    }

    private static String toGermanPanelColorDisplayName(DyeColor color) {
        return switch (color) {
            case WHITE -> "Weißes";
            case LIGHT_GRAY -> "Hellgraues";
            case GRAY -> "Graues";
            case BLACK -> "Schwarzes";
            case BROWN -> "Braunes";
            case RED -> "Rotes";
            case ORANGE -> "Oranges";
            case YELLOW -> "Gelbes";
            case LIME -> "Hellgrünes";
            case GREEN -> "Grünes";
            case CYAN -> "Türkises";
            case LIGHT_BLUE -> "Hellblaues";
            case BLUE -> "Blaues";
            case PURPLE -> "Violettes";
            case MAGENTA -> "Magentafarbenes";
            case PINK -> "Rosa";
        };
    }
}
