package de.pasuki.colorful_redstone_lamps.fabric;

import de.pasuki.colorful_redstone_lamps.config.ModConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ColorfulRedstoneLampsFabricConfig {
    private static final String FILE_NAME = "colorful_redstone_lamps.properties";
    private static final String KEY_SHOW_WELCOME_MESSAGE = "showWelcomeMessage";

    private ColorfulRedstoneLampsFabricConfig() {
    }

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        loadFromPath(configPath);
    }

    private static void loadFromPath(Path configPath) {
        Properties defaults = createDefaults();
        Properties properties = new Properties(defaults);

        try {
            if (Files.notExists(configPath)) {
                Files.createDirectories(configPath.getParent());
                writeDefaults(configPath, defaults);
            } else {
                try (InputStream inputStream = Files.newInputStream(configPath)) {
                    properties.load(inputStream);
                }
            }
        } catch (IOException ignored) {
        }

        boolean showWelcome = Boolean.parseBoolean(properties.getProperty(KEY_SHOW_WELCOME_MESSAGE, "true"));
        ModConfig.setShowWelcomeMessage(showWelcome);
    }

    private static Properties createDefaults() {
        Properties defaults = new Properties();
        defaults.setProperty(KEY_SHOW_WELCOME_MESSAGE, "true");
        return defaults;
    }

    private static void writeDefaults(Path configPath, Properties defaults) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            defaults.store(outputStream, "Colorful Redstone Lamps");
        }
    }
}
