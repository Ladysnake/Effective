package ladysnake.illuminations.client.config;

import ladysnake.illuminations.client.enums.WillOWispsSpawnRate;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class Config {
	public static final Path PROPERTIES_PATH = FabricLoader.getInstance().getConfigDir().resolve("illuminations.properties");
	private static final Properties config = new Properties() {
		@Override
		public @NotNull Set<Map.Entry<Object, Object>> entrySet() {
			Iterator<Map.Entry<Object, Object>> iterator = super.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().toString())).iterator();

			Set<Map.Entry<Object, Object>> temp = new LinkedHashSet<>(super.entrySet().size());
			while (iterator.hasNext())
				temp.add(iterator.next());

			return temp;
		}
	};
	private static HalloweenFeatures halloweenFeatures;
	private static WillOWispsSpawnRate willOWispsSpawnRate;
	private static int density;
	private static boolean viewAurasFP;
	private static boolean debugMode;
	private static boolean displayCosmetics;
	private static boolean displayDonationToast;

	public static void load() {
		// if illuminations.properties exist, load it
		if (Files.isRegularFile(PROPERTIES_PATH)) {
			// load illuminations.properties
			try {
				config.load(Files.newBufferedReader(PROPERTIES_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // if no illuminations.properties, load default values
			// define default properties
			setHalloweenFeatures(DefaultConfig.HALLOWEEN_FEATURES);
			setWillOWispsSpawnRate(DefaultConfig.WILL_O_WISPS_SPAWN_RATE);
			setDensity(DefaultConfig.DENSITY);
			setDisplayCosmetics(DefaultConfig.DISPLAY_COSMETICS);
			setViewAurasFP(DefaultConfig.VIEW_AURAS_FP);
			setDisplayDonationToast(DefaultConfig.DISPLAY_DONATION_TOAST);

            /*
            auraSettings = new HashMap<>();
            defaultAuraSettings.forEach(Config::setAuraSettings);
            */

			Config.save();
			return;
		}

		parseProperty("halloween-features", Config::setHalloweenFeatures, DefaultConfig.HALLOWEEN_FEATURES);
		parseProperty("will-o-wisps-spawn-rate", Config::setWillOWispsSpawnRate, DefaultConfig.WILL_O_WISPS_SPAWN_RATE);
		parseProperty("density", Config::setDensity, DefaultConfig.DENSITY);
		parseProperty("display-cosmetics", Config::setDisplayCosmetics, DefaultConfig.DISPLAY_COSMETICS);
		parseProperty("debug-mode", Config::setDebugMode, DefaultConfig.DEBUG_MODE);
		parseProperty("view-auras-fp", Config::setViewAurasFP, DefaultConfig.VIEW_AURAS_FP);
		parseProperty("display-donation-toast", Config::setDisplayDonationToast, DefaultConfig.DISPLAY_DONATION_TOAST);

        /*
        auraSettings = new HashMap<>();
        defaultAuraSettings.forEach((aura, v) ->
                setAuraSettings(aura,
                        tryOrDefault(() -> {
                            String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
                            float s = Float.parseFloat(config.getProperty(name + "-aura-spawn-rate"));
                            int d = Integer.parseInt(config.getProperty(name + "-aura-delay"));
                            return new AuraSettings(s, d);
                        }, v)));
        */

		Config.save();
	}

	private static <T extends Enum<T>> void parseProperty(String property, Consumer<T> setter, T defaultValue) {
		try {
			setter.accept(Enum.valueOf(defaultValue.getDeclaringClass(), config.getProperty(property)));
		} catch (Exception e) {
			setter.accept(defaultValue);
		}
	}

	private static void parseProperty(String property, Consumer<Boolean> setter, Boolean defaultValue) {
		try {
			setter.accept(Boolean.parseBoolean(config.getProperty(property)));
		} catch (Exception e) {
			setter.accept(defaultValue);
		}
	}

	private static void parseProperty(String property, Consumer<Integer> setter, Integer defaultValue) {
		try {
			setter.accept(Integer.parseInt(config.getProperty(property)));
		} catch (Exception e) {
			setter.accept(defaultValue);
		}
	}

	public static void save() {
		try {
			config.store(Files.newBufferedWriter(Config.PROPERTIES_PATH), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//region Getters/Setters

	public static HalloweenFeatures getHalloweenFeatures() {
		return halloweenFeatures;
	}

	public static void setHalloweenFeatures(HalloweenFeatures value) {
		halloweenFeatures = value;
		config.setProperty("halloween-features", value.toString());
	}

	public static WillOWispsSpawnRate getWillOWispsSpawnRate() {
		return willOWispsSpawnRate;
	}

	public static void setWillOWispsSpawnRate(WillOWispsSpawnRate value) {
		willOWispsSpawnRate = value;
		config.setProperty("will-o-wisps-spawn-rate", value.name());
	}

	public static int getDensity() {
		return density;
	}

	public static void setDensity(int value) {
		density = value;
		config.setProperty("density", Integer.toString(value));
	}

	public static boolean getViewAurasFP() {
		return viewAurasFP;
	}

	public static void setViewAurasFP(boolean value) {
		viewAurasFP = value;
		config.setProperty("view-auras-fp", Boolean.toString(value));
	}

	public static boolean shouldDisplayCosmetics() {
		return displayCosmetics;
	}

	public static void setDisplayCosmetics(boolean value) {
		displayCosmetics = value;
		config.setProperty("display-cosmetics", Boolean.toString(value));
	}

	public static boolean isDebugMode() {
		return debugMode;
	}

	public static void setDebugMode(boolean value) {
		debugMode = value;
		config.setProperty("debug-mode", Boolean.toString(value));
	}

	public static boolean shouldDisplayDonationToast() {
		return displayDonationToast;
	}

	public static void setDisplayDonationToast(boolean value) {
		displayDonationToast = value;
		config.setProperty("display-donation-toast", Boolean.toString(value));
	}

    /*
    public static Map<String, AuraSettings> getAuraSettings()
    {
        return auraSettings;
    }

    public static AuraSettings getAuraSettings(String aura)
    {
        return auraSettings.get(aura);
    }

    public static void setAuraSettings(String aura, AuraSettings settings)
    {
        auraSettings.put(aura, settings);
        String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
        config.setProperty(name + "-aura-spawn-rate", Float.toString(settings.spawnRate()));
        config.setProperty(name + "-aura-delay", Integer.toString(settings.delay()));
    }

    public static void setAuraSpawnRate(String aura, float value)
    {
        AuraSettings settings = auraSettings.get(aura);
        auraSettings.put(aura, settings.withSpawnRate(value));
        String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
        config.setProperty(name + "-aura-spawn-rate", Float.toString(value));
    }

    public static void setAuraDelay(String aura, int value)
    {
        AuraSettings settings = auraSettings.get(aura);
        auraSettings.put(aura, settings.withDelay(value));
        String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
        config.setProperty(name + "-aura-delay", Integer.toString(value));
    }*/

	//endregion
}
