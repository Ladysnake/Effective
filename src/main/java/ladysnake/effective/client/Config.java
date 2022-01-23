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

    private static final String WATERFALL_HEIGHT_LOWER_LIMIT = "waterfall-height-lower-limit";
    public static int waterfallHeightLowerLimit = 3;

    private static final String WATERFALL_HEIGHT_UPPER_LIMIT = "waterfall-height-upper-limit";
    public static int waterfallHeightUpperLimit = 13;

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
        props.setProperty(WATERFALL_HEIGHT_LOWER_LIMIT, String.valueOf(waterfallHeightLowerLimit));
        props.setProperty(WATERFALL_HEIGHT_UPPER_LIMIT, String.valueOf(waterfallHeightUpperLimit));
    }

    public static void assign(@NotNull Properties props) {
        enableSplashParticles = defaultBoolean(props.getProperty(ENABLE_SPLASH_PARTICLES), true);
        enableWaterfallParticles = defaultBoolean(props.getProperty(ENABLE_WATERFALL_PARTICLES), true);
        waterfallHeightLowerLimit = defaultInt(props.getProperty(WATERFALL_HEIGHT_LOWER_LIMIT), 3);
        waterfallHeightUpperLimit = defaultInt(props.getProperty(WATERFALL_HEIGHT_UPPER_LIMIT), 3);
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean defaultBoolean(String bool, boolean defaultOption) {
        return bool == null ? defaultOption : Boolean.parseBoolean(bool);
    }

    private static int defaultInt(String integer, int defaultOption){
        return integer == null ? defaultOption : Integer.parseInt(integer);
    }
}