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

package it.mikeslab.labutil.inventories.component;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.mikeslab.labutil.inventories.event.ItemFinalizingExecutor;
import it.mikeslab.labutil.inventories.inventory.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.AbstractMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class CustomInventory {

    private Map<Integer, Map.Entry<CustomItem, String>> items;
    private Material fillerMaterial;
    private String name;
    private String title;
    private int size;
    private InventoryHolder holder;
    private Builder builder;

    public void addItem(int slot, CustomItem item, String action) {
        items.put(slot, new AbstractMap.SimpleEntry<>(item, action));
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public Inventory getBukkitInventory() {
        Inventory inventory = Bukkit.createInventory(holder, size, title);

        for(Map.Entry<Integer, Map.Entry<CustomItem, String>> cell : items.entrySet()) {

            int slot = cell.getKey();
            CustomItem item = new ItemFinalizingExecutor(name).loadItemFinalizingExecutor(cell.getValue().getKey(), slot);



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
