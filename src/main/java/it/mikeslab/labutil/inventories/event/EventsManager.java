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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class EventsManager implements Listener {
    private EventCloseExecutor eventCloseExecutor;
    private EventOpenExecutor eventOpenExecutor;
    private EventClickExecutor eventClickExecutor;
    private InventoryHolder holder;

    public EventsManager init(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return this;
    }

    public EventsManager registerEvents(String inventoryName, CustomInventory customInventory) {

        this.holder = customInventory.getHolder();
        this.eventCloseExecutor = new EventCloseExecutor(inventoryName);
        this.eventOpenExecutor = new EventOpenExecutor(inventoryName);
        this.eventClickExecutor = new EventClickExecutor(inventoryName, customInventory);
        return this;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(event.getInventory().getHolder() == this.holder) {
            eventCloseExecutor.loadCloseEvent().accept(event);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if(event.getInventory().getHolder() == this.holder) {
            eventOpenExecutor.loadOpenEvent().accept(event);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getHolder() == this.holder) {
            event.setCancelled(true);
            eventClickExecutor.loadClickEvent().accept(event);
        }
    }


}
