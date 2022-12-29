package it.mikeslab.labutil.inventories;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class Holder implements InventoryHolder {

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
