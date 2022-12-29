package it.mikeslab.labutil.inventories;

import it.mikeslab.labutil.inventories.component.CustomItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class CustomInventory {

    private Map<Integer, CustomItem> items;
    private String name;
    private String title;
    private int size;

    public void addItem(int slot, CustomItem item) {
        items.put(slot, item);
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public Inventory getBukkitInventory() {
        Inventory inventory = Bukkit.createInventory(new Holder(), size, title);
        for(Map.Entry<Integer, CustomItem> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), CustomItem.fromCustom(entry.getValue()));
        }
        return inventory;
    }
}
