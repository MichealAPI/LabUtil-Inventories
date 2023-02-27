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
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventsManager implements Listener {

    public static Map<Object, String> instances = new HashMap<>();
    private Map<InventoryHolder, EventCloseExecutor> closeExecutor;
    private Map<InventoryHolder, EventOpenExecutor> openExecutor;
    private Map<InventoryHolder, EventClickExecutor> clickExecutor;
    private Map<InventoryHolder, ItemFinalizingExecutor> itemFinalizingExecutor;
    private List<InventoryHolder> holder;

    public EventsManager init(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return this;
    }

    public EventsManager registerEvent(Object eventClassInstance, CustomInventory customInventory) {

        if(closeExecutor == null) closeExecutor = new HashMap<>();
        if(openExecutor == null) openExecutor = new HashMap<>();
        if(clickExecutor == null) clickExecutor = new HashMap<>();
        if(itemFinalizingExecutor == null) itemFinalizingExecutor = new HashMap<>();
        if(holder == null) holder = new ArrayList<>();
        String inventoryName = customInventory.getName();

        registerEventInstance(eventClassInstance, inventoryName);

        InventoryHolder holder = customInventory.getHolder();
        this.holder.add(customInventory.getHolder());
        this.closeExecutor.put(holder, new EventCloseExecutor(inventoryName));
        this.openExecutor.put(holder, new EventOpenExecutor(inventoryName));
        this.clickExecutor.put(holder, new EventClickExecutor(inventoryName, customInventory));
        return this;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = match(event.getInventory().getHolder());
        if (holder == null) return;

        closeExecutor.get(holder).loadCloseEvent().accept(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        InventoryHolder holder = match(event.getInventory().getHolder());
        if (holder == null) return;

        openExecutor.get(holder).loadOpenEvent().accept(event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;

        InventoryHolder holder = match(event.getInventory().getHolder());
        if (holder == null) return;

        clickExecutor.get(holder).loadClickEvent().accept(event);
    }


    private InventoryHolder match(InventoryHolder holder) {
        for(InventoryHolder h : this.holder) {
            if(h.equals(holder)) {
                return h;
            }
        }
        return null;
    }

    private void registerEventInstance(Object object, String inventoryName) {
        instances.put(object, inventoryName);
    }

    public void unregisterEvent(Object object) {
        instances.remove(object);
    }


}
