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

package it.mikeslab.labutil.inventories.inventory;

import com.cryptomorin.xseries.XMaterial;
import it.mikeslab.labutil.inventories.cache.InvData;
import it.mikeslab.labutil.inventories.component.CustomInventory;
import it.mikeslab.labutil.inventories.component.CustomItem;
import it.mikeslab.labutil.inventories.component.Translatable;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.InventoryHolder;

import java.util.*;

@Getter
@Setter
public class Builder {
    private final String name;
    private final YamlConfiguration config;
    private final InventoryHolder holder;
    private CustomInventory inventory;
    private Material fillerMaterial;
    private List<Translatable> translatableList;
    private Map<String, String> extras;

    public Builder(String name, InventoryHolder holder) {
        this.translatableList = new ArrayList<>();
        this.name = name;
        this.config = InvData.CACHE.get(name);
        this.holder = holder;
    }

    public CustomInventory build(Map<String, String> extras) {
        Map<Integer, Map.Entry<CustomItem, String>> items = getItems(extras);
        String title = LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(config.getString("title")));
        int size = config.getInt("size");
        this.fillerMaterial = getFillerMaterial();

        this.inventory = new CustomInventory(items, fillerMaterial, name, title, size, holder, this);
        return inventory;
    }


    private Map<Integer, Map.Entry<CustomItem, String>> getItems(Map<String, String> extras) {
        Map<Integer, Map.Entry<CustomItem, String>> items = new HashMap<>();
        for(String key : config.getConfigurationSection("items").getKeys(false)) {
            int slot = Integer.parseInt(key); //Throws NumberFormatException if key is not a number
            String action = getActionValue(key);

            CustomItem item = CustomItem.fromConfig(config.getConfigurationSection("items." + key), translatableList, extras.getOrDefault(action, ""));

            items.put(slot, new AbstractMap.SimpleEntry<>(item, action));
        }
        return items;
    }


    private String getActionValue(String key) {
        String action = config.getString("items." + key + ".action");
        return action == null ? "NONE" : action;
    }

    private Material getFillerMaterial() {
        String filler = config.getString("filler");
        return filler == null ? XMaterial.AIR.parseMaterial() : XMaterial.matchXMaterial(filler).get().parseMaterial();
    }

    public static String getAction(Map<Integer, Map.Entry<CustomItem, String>> items, int slot) {
        return items.get(slot).getValue();
    }







}
