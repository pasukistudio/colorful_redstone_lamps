package de.pasuki.colorful_redstone_lamps.config;

public final class ModConfig {
    private static volatile boolean showWelcomeMessage = true;

    private ModConfig() {
    }

    public static boolean showWelcomeMessage() {
        return showWelcomeMessage;
    }

    public static void setShowWelcomeMessage(boolean value) {
        showWelcomeMessage = value;
    }
}
