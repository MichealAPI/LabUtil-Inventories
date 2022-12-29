package it.mikeslab.labutil.inventories.event;

import it.mikeslab.labutil.inventories.CustomInventory;
import it.mikeslab.labutil.inventories.Holder;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class EventsManager implements Listener {
    private final JavaPlugin plugin;
    private EventCloseExecutor eventCloseExecutor;
    private EventOpenExecutor eventOpenExecutor;
    private EventClickExecutor eventClickExecutor;

    public EventsManager registerEvents(String inventoryName, CustomInventory customInventory) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.eventCloseExecutor = new EventCloseExecutor(inventoryName);
        this.eventOpenExecutor = new EventOpenExecutor(inventoryName);
        this.eventClickExecutor = new EventClickExecutor(inventoryName, customInventory);
        return this;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(event.getInventory().getHolder() instanceof Holder) {
            eventCloseExecutor.loadCloseEvent().accept(event);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if(event.getInventory().getHolder() instanceof Holder) {
            eventOpenExecutor.loadOpenEvent().accept(event);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getHolder() instanceof Holder) {
            eventClickExecutor.loadClickEvent().accept(event);
        }
    }


}
