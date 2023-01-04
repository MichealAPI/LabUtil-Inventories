/*
 * Copyright (c) 2023 MikesLab
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
            File file = new File(plugin.getDataFolder() + File.separator + subFolder + File.separator + inventory + ".yml");
            if (!file.exists()) {
                plugin.saveResource(subFolder + File.separator + inventory + ".yml", false);
            }

            CACHE.put(inventory, YamlConfiguration.loadConfiguration(file));
        }
    }
}
