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

package it.mikeslab.labutil.inventories.event;

import it.mikeslab.labutil.inventories.component.CustomInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class EventDispatcher implements Listener {

    private Map<String, Map.Entry<Object, CustomInventory>> listeners;
    private Map<String, CustomInventory> inventoryMap;

    public void init(Map<String, CustomInventory> inventories) {
        this.inventoryMap = inventories;
    }

    public void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.listeners = new HashMap<>();
    }

    public void assignEvent(Object eventClassInstance, CustomInventory customInventory) {
        InventoryHolder holder = new Holder(customInventory.getName());
        customInventory.setHolder(holder);

        String inventoryName = customInventory.getName();

        listeners.put(inventoryName, new HashMap.SimpleEntry<>(eventClassInstance, customInventory));
    }

    @EventHandler
    private void onOpen(InventoryOpenEvent event) {
        CustomInventory customInventory = isCustomInventory(event.getInventory().getHolder());

        if(customInventory == null) return;
        String inventoryName = customInventory.getName();

        try {
            MethodAccessUtil.invokeMethod(listeners.get(inventoryName).getKey(), "onOpen", event, customInventory);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    @EventHandler
    private void onClose(InventoryOpenEvent event) {
        CustomInventory customInventory = isCustomInventory(event.getInventory().getHolder());

        if(customInventory == null) return;
        String inventoryName = customInventory.getName();

        try {
            MethodAccessUtil.invokeMethod(listeners.get(inventoryName).getKey(), "onClose", event, customInventory);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }



    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getHolder() == null) return;
        if(!(event.getClickedInventory().getHolder() instanceof Holder)) return;

        Holder holder = (Holder) event.getClickedInventory().getHolder();
        CustomInventory inventory = identifyInventory(holder);

        if(inventory == null) return;
        event.setCancelled(true);

        int slot = event.getSlot();
        String action = (inventory.getItems().get(slot).getValue() + "").toLowerCase();
        if(action.equals("")) return;

        Object listenerInstance = listeners.get(inventory.getName()).getKey();

        try {
            MethodAccessUtil.invokeMethod(listenerInstance, action, event);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    private CustomInventory isCustomInventory(InventoryHolder holder) {
        if (holder instanceof Holder) {
            Holder holder1 = (Holder) holder;
            return inventoryMap.get(holder1.getInventoryName());
        }
        return null;
    }


    private CustomInventory identifyInventory(Holder holder) {
        return inventoryMap.getOrDefault(holder.getInventoryName(), null);
    }


}
