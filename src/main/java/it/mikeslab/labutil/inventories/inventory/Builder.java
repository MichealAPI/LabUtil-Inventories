package it.mikeslab.labutil.inventories.inventory;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.mikeslab.labutil.inventories.cache.InvData;
import it.mikeslab.labutil.inventories.component.CustomInventory;
import it.mikeslab.labutil.inventories.component.CustomItem;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.InventoryHolder;

@Getter
@Setter
public class Builder {
    private final String name;
    private final YamlConfiguration config;
    private CustomInventory inventory;
    private InventoryHolder holder;
    private Material fillerMaterial;

    public Builder(String name, InventoryHolder holder) {
        this.name = name;
        this.config = InvData.CACHE.get(name);
        this.holder = holder;
    }

    public CustomInventory build() {
        HashBasedTable<Integer, CustomItem, String> items = getItems();
        String title = LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(config.getString("title")));
        int size = config.getInt("size");
        this.fillerMaterial = getFillerMaterial();

        this.inventory = new CustomInventory(items, fillerMaterial, name, title, size, holder);
        return inventory;
    }


    private HashBasedTable<Integer, CustomItem, String> getItems() {
        HashBasedTable<Integer, CustomItem, String> items = HashBasedTable.create();
        for(String key : config.getConfigurationSection("items").getKeys(false)) {
            int slot = Integer.parseInt(key); //Throws NumberFormatException if key is not a number
            String action = getActionValue(key);
            CustomItem item = CustomItem.fromConfig(config.getConfigurationSection("items." + key));

            items.put(slot, item, action);
            System.out.println("Added item " + item.getDisplayName() + " to slot " + slot + " with action " + action);
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

    public static String getAction(HashBasedTable<Integer, CustomItem, String> items, int slot) {
        String action = null;
        for (Table.Cell<Integer, CustomItem, String> cell : items.cellSet()) {
            if (cell.getRowKey().equals(slot)) {
                action = cell.getValue();
                break;
            }
        }
        return action;
    }







}
