package it.mikeslab.labutil.inventories.cache;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InvData {
    private final String subFolder;
    private final String[] inventories;
    private final JavaPlugin plugin;
    public static Map<String, YamlConfiguration> CACHE = new HashMap<>();

    public void load() {
        File destinationDirectory = new File(plugin.getDataFolder() + File.separator + subFolder);

        // Create the destination directory if it doesn't exist
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }

        // Get a list of all the files in the plugin's resources directory

        // Copy each file to the destination directory
        for (String inventory : inventories) {
            plugin.saveResource(subFolder + File.separator + inventory + ".yml", false);
            CACHE.put(inventory, YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + subFolder + File.separator + inventory + ".yml")));
        }
    }
}
