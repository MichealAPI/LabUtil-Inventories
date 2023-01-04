package it.mikeslab.labutil.inventories.component;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
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

    private HashBasedTable<String, CustomItem, Integer> items;
    private Material fillerMaterial;
    private String name;
    private String title;
    private int size;
    private InventoryHolder holder;

    public void addItem(int slot, CustomItem item, String action) {
        items.put(action, item, slot);
    }

    public void removeItem(String action, CustomItem item) {
        items.remove(action, item);
    }

    public Inventory getBukkitInventory() {
        Inventory inventory = Bukkit.createInventory(holder, size, title);

        for(Table.Cell<String, CustomItem, Integer> cell : items.cellSet()) {
            CustomItem item = cell.getColumnKey();
            int slot = cell.getValue();

            inventory.setItem(slot, CustomItem.fromCustom(item));
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

        ItemStack filler = XMaterial.matchXMaterial(fillerMaterial).parseItem();
        if(fillerMaterial.isAir()) return filler;


        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");

        filler.setItemMeta(meta);
        return filler;
    }


}
