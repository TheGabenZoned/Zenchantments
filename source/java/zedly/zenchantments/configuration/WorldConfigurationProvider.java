package zedly.zenchantments.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import zedly.zenchantments.ZenchantmentsPlugin;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import zedly.zenchantments.api.Zenchantments;

public class WorldConfigurationProvider implements zedly.zenchantments.api.configuration.WorldConfigurationProvider {

    private static final Map<UUID, WorldConfiguration> configMap = new HashMap<>();
    private static final WorldConfigurationProvider instance = new WorldConfigurationProvider();

    public static WorldConfigurationProvider getInstance() {
        return instance;
    }

    @Override
    @NotNull
    public WorldConfiguration getConfigurationForWorld(final @NotNull World world) {
        return configMap.computeIfAbsent(world.getUID(), ignored -> tryLoadConfigurationForWorld(world));
    }

    @Override
    @NotNull
    public WorldConfiguration loadConfigurationForWorld(final @NotNull World world) throws IOException, InvalidConfigurationException {
        WorldConfiguration newConfiguration = this.loadConfiguration(world.getName());
        this.configMap.put(world.getUID(), newConfiguration);
        return newConfiguration;
    }

    private WorldConfiguration loadDefaultConfigurationForWorld(final @NotNull World world) {
        WorldConfiguration newConfiguration = GlobalConfiguration.getDefaultWorldConfiguration();
        this.configMap.put(world.getUID(), newConfiguration);
        return newConfiguration;
    }

    @Override
    public void resetConfigurationForWorld(final @NotNull World world) {
        configMap.put(world.getUID(), tryLoadConfigurationForWorld(world));
    }

    public void loadWorldConfigurations() {
        Bukkit.getServer().getWorlds().forEach(this::tryLoadConfigurationForWorld);
    }

    private WorldConfiguration tryLoadConfigurationForWorld(final @NotNull World world) {
        try {
            return loadConfigurationForWorld(world);
        } catch (IOException | InvalidConfigurationException e) {
            System.err.println("Zenchantments was unable to load the configuration for world: " + world.getName() + ".\n" +
                "Please check the configuration for this world. Falling back to default configuration!");
            e.printStackTrace();
            return loadDefaultConfigurationForWorld(world);
        }
    }

    @NotNull
    private WorldConfiguration loadConfiguration(final @NotNull String worldName) throws IOException, InvalidConfigurationException {
        // Create default config for this world if it doesn't exist
        Path path = Path.of(ZenchantmentsPlugin.getInstance().getDataFolder().getAbsolutePath(), worldName + ".yml");
        File file = path.toFile();
        if (!file.exists()) {
            try (InputStream stream = Zenchantments.class.getResourceAsStream("/config.yml")) {
                if (stream == null) {
                    throw new IOException("Missing bundled config.yml");
                }
                Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Load the config for this world
        YamlConfiguration yamlConfig = new YamlConfiguration();
        yamlConfig.load(file);
        if (mergeMissingEnchantments(yamlConfig)) {
            yamlConfig.save(file);
        }

        return WorldConfiguration.fromYamlConfiguration(yamlConfig);
    }

    private boolean mergeMissingEnchantments(final @NotNull YamlConfiguration yamlConfig) throws IOException {
        try (InputStream stream = Zenchantments.class.getResourceAsStream("/config.yml")) {
            if (stream == null) {
                throw new IOException("Missing bundled config.yml");
            }

            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                new InputStreamReader(stream, StandardCharsets.UTF_8)
            );
            List<Object> enchantments = new ArrayList<>(yamlConfig.getList("enchantments", List.of()));
            Set<String> configuredNames = new HashSet<>();
            for (Object partObject : enchantments) {
                if (partObject instanceof Map<?, ?> part) {
                    configuredNames.addAll(part.keySet().stream().map(Object::toString).toList());
                }
            }

            boolean changed = false;
            for (Object partObject : defaults.getList("enchantments", List.of())) {
                if (!(partObject instanceof Map<?, ?> part)) {
                    continue;
                }
                for (Object name : part.keySet()) {
                    if (configuredNames.add(name.toString())) {
                        enchantments.add(partObject);
                        changed = true;
                    }
                }
            }

            if (changed) {
                yamlConfig.set("enchantments", enchantments);
            }
            return changed;
        }
    }
}
