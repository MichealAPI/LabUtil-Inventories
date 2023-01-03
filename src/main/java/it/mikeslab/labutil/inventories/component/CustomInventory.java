package it.mikeslab.labutil.inventories.component;

import it.mikeslab.labutil.inventories.component.CustomItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class CustomInventory {

    private Map<Integer, CustomItem> items;
    private Material fillerMaterial;
    private String name;
    private String title;
    private int size;
    private InventoryHolder holder;

    public void addItem(int slot, CustomItem item) {
        items.put(slot, item);
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public Inventory getBukkitInventory() {
        Inventory inventory = Bukkit.createInventory(holder, size, title);

        for(Map.Entry<Integer, CustomItem> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), CustomItem.fromCustom(entry.getValue()));
        }

        return this.fill(inventory);
    }

    private Inventory fill(Inventory inventory) {
        ItemStack filler = formulateFiller();
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }


        return inventory;
    }


    private ItemStack formulateFiller() {
        ItemStack filler = new ItemStack(fillerMaterial);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");

        filler.setItemMeta(meta);
        return filler;
    }


}
