package it.mikeslab.labutil.inventories.event;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ExecutorUtil {
    public static Map<Object, String> instances = new HashMap<>();

    public static EventsManager registerEvent(Object object, String inventoryName, JavaPlugin plugin) {
        instances.put(object, inventoryName);
        return new EventsManager().init(plugin);
    }

    public static void unregisterEvent(Object object) {
        instances.remove(object);
    }




}
