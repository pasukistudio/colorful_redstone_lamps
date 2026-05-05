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

            entries.put("block.colorful_redstone_lamps." + baseId, colorName + " Redstone Lamp");
            entries.put("block.colorful_redstone_lamps." + baseId + "_inverted", colorName + " Redstone Lamp (Inverted)");
            entries.put("item.colorful_redstone_lamps." + baseId, colorName + " Redstone Lamp");
            entries.put("item.colorful_redstone_lamps." + baseId + "_inverted", colorName + " Redstone Lamp (Inverted)");
        }

        entries.put("itemGroup.colorful_redstone_lamps.normal_lamps", "Colorful Redstone Lamps");
        entries.put("itemGroup.colorful_redstone_lamps.inverted_lamps", "Inverted Colorful Redstone Lamps");
        entries.put("tooltip.colorful_redstone_lamps.lamp.normal",   "Turns on with a redstone signal.");
        entries.put("tooltip.colorful_redstone_lamps.lamp.inverted", "Turns off with a redstone signal.");
        entries.put("colorful_redstone_lamps.welcome.title", "Colorful Redstone Lamps loaded.");
        entries.put("colorful_redstone_lamps.welcome.feedback_intro", "Feedback:");
        entries.put("colorful_redstone_lamps.welcome.feedback.github", "Github");
        entries.put("colorful_redstone_lamps.welcome.feedback.discord", "Discord");
        entries.put("config.colorful_redstone_lamps.show_welcome_message", "Show Welcome Message");


        return entries;
    }

    public static Map<String, String> germanDe() {
        Map<String, String> entries = new LinkedHashMap<>();

        for (DyeColor color : DyeColor.values()) {
            String colorName = toGermanColorDisplayName(color);
            String baseId = color.getName() + "_redstone_lamp";

            entries.put("block.colorful_redstone_lamps." + baseId, colorName + " Redstone-Lampe");
            entries.put("block.colorful_redstone_lamps." + baseId + "_inverted", colorName + " invertierte Redstone-Lampe");
            entries.put("item.colorful_redstone_lamps." + baseId, colorName + " Redstone-Lampe");
            entries.put("item.colorful_redstone_lamps." + baseId + "_inverted", colorName + " invertierte Redstone-Lampe");
        }

        entries.put("itemGroup.colorful_redstone_lamps.normal_lamps", "Bunte Redstone-Lampen");
        entries.put("itemGroup.colorful_redstone_lamps.inverted_lamps", "Bunte Redstone-Lampen Invertiert");
        entries.put("tooltip.colorful_redstone_lamps.lamp.normal",   "Schaltet sich bei Redstone-Signal ein.");
        entries.put("tooltip.colorful_redstone_lamps.lamp.inverted", "Schaltet sich bei Redstone-Signal aus.");
        entries.put("colorful_redstone_lamps.welcome.title", "Colorful Redstone Lamps geladen.");
        entries.put("colorful_redstone_lamps.welcome.feedback_intro", "Feedback:");
        entries.put("colorful_redstone_lamps.welcome.feedback.github", "Github");
        entries.put("colorful_redstone_lamps.welcome.feedback.discord", "Discord");
        entries.put("config.colorful_redstone_lamps.show_welcome_message", "Willkommensnachricht anzeigen");

        return entries;
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
}
