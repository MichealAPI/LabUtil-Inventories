package it.mikeslab.labutil.inventories.inventory;

import it.mikeslab.labutil.inventories.CustomInventory;
import it.mikeslab.labutil.inventories.cache.InvData;
import it.mikeslab.labutil.inventories.component.CustomItem;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Builder {
    private final String name;
    private final YamlConfiguration config;
    private CustomInventory inventory;

    public Builder(String name) {
        this.name = name;
        this.config = InvData.CACHE.get(name);
    }

    public CustomInventory build() {
        Map<Integer, CustomItem> items = getItems();
        String title = LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(config.getString("title")));
        int size = config.getInt("size");

        this.inventory = new CustomInventory(items, name, title, size);
        return inventory;
    }


    private Map<Integer, CustomItem> getItems() {
        Map<Integer, CustomItem> items = new HashMap<>();
        for(String key : config.getConfigurationSection("items").getKeys(false)) {
            int slot = Integer.parseInt(key); //Throws NumberFormatException if key is not a number
            items.put(slot, CustomItem.fromConfig(config.getConfigurationSection("items." + slot)));
        }
        return items;
    }

    public static String getActionFromSlot(int slot, CustomInventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            CustomItem item = inventory.getItems().get(i);
            if (item != null && item.getSlot() == slot) {
                return item.getAction();
            }
        }
        return null;
    }







}
