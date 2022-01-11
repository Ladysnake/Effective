package ladysnake.effective.client;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("effective.properties");

    private static final String ENABLE_SPLASH_PARTICLES = "enable-splash-particles";
    public static boolean enableSplashParticles = true;

    private static final String ENABLE_WATERFALL_PARTICLES = "enable-waterfall-particles";
    public static boolean enableWaterfallParticles = true;

    private static final String WATERFALL_VOLUME = "waterfall-volume";
    public static float waterfallVolume = 2.5f;

    private static final String MIN_WATERFALL_HEIGHT = "minimum_waterfall-height";
    public static float waterfallHeight = 5.0f;


    public static void save() {
        Properties props = new Properties();
        read(props);

        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
            props.store(out, "Effective Configuration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Properties props = new Properties();
        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
                save();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (InputStream stream = Files.newInputStream(CONFIG_PATH)) {
            props.load(stream);
            assign(props);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void read(@NotNull Properties props) {
        props.setProperty(ENABLE_SPLASH_PARTICLES, String.valueOf(enableSplashParticles));
        props.setProperty(ENABLE_WATERFALL_PARTICLES, String.valueOf(enableWaterfallParticles));
        props.setProperty(WATERFALL_VOLUME, String.valueOf(waterfallVolume));
        props.setProperty(MIN_WATERFALL_HEIGHT, String.valueOf(waterfallHeight));
    }

    public static void assign(@NotNull Properties props) {
        enableSplashParticles = defaultBoolean(props.getProperty(ENABLE_SPLASH_PARTICLES), true);
        enableWaterfallParticles = defaultBoolean(props.getProperty(ENABLE_WATERFALL_PARTICLES), true);
        waterfallVolume = defaultFloat(props.getProperty(WATERFALL_VOLUME), waterfallVolume);
        waterfallHeight = defaultFloat(props.getProperty(MIN_WATERFALL_HEIGHT), waterfallHeight);
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean defaultBoolean(String bool, boolean defaultOption) {
        return bool == null ? defaultOption : Boolean.parseBoolean(bool);
    }

    @SuppressWarnings("SameParameterValue")
    private static float defaultFloat(String amount, float defaultOption) {
        return amount == null ? defaultOption : Float.parseFloat(amount);
    }

    @SuppressWarnings("SameParameterValue")
    private static int defaultInt(String amount, int defaultOption) {
        return amount == null ? defaultOption : Integer.parseInt(amount);
    }
}